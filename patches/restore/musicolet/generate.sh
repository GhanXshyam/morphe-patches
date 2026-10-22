#!/usr/bin/env bash
set -euo pipefail

here=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
resources=$here/../../src/main/resources/musicolet/restored
class=MusicoletRestored

build_tools_version=${BUILD_TOOLS_VERSION:-37.0.0}
platform_version=${PLATFORM_VERSION:-37.0}

sdk=${ANDROID_HOME:-$HOME/Android/Sdk}
build_tools=${BUILD_TOOLS:-$sdk/build-tools/$build_tools_version}
platform=${PLATFORM:-$sdk/platforms/android-$platform_version/android.jar}
javac=${JAVAC:-/usr/lib/jvm/java-21-openjdk/bin/javac}

die() {
    echo "$*" >&2
    exit 1
}

hashes() {
    grep -E '^[0-9a-f]{64}  ' "$1"
}

[ -x "$build_tools/d8" ] || die "no d8 at $build_tools; install build-tools $build_tools_version or set BUILD_TOOLS"
[ -f "$platform" ] || die "no android.jar at $platform; install platform $platform_version or set PLATFORM"
[ -x "$javac" ] || die "no javac at $javac"
javac_version=$("$javac" -version 2>&1 | cut -d' ' -f2)
[ "${javac_version%%.*}" = 21 ] || die "javac $javac_version at $javac is not JDK 21"
command -v baksmali >/dev/null || die "baksmali is not on the PATH"

verify=false
[ "${1:-}" = "--verify" ] && verify=true

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT

out=$resources
$verify && out=$work/resources
mkdir -p "$out"

"$javac" -nowarn -source 8 -target 8 -d "$work/classes" -cp "$platform" "$here"/stub/*.java "$here/$class.java"
"$build_tools/d8" --release --min-api 23 --lib "$platform" --classpath "$work/classes" --output "$work" "$work/classes/$class.class"
baksmali d --sequential-labels -o "$work/smali" "$work/classes.dex"

awk -v out="$out" '
    /^\.method public static / {
        split($4, signature, "(")
        file = out "/" signature[1] ".smali"
        body = 1
        next
    }
    /^\.end method/ { body = 0; close(file); next }
    body && /^ *\.(line|prologue|locals) / { next }
    body { sub(/^ +/, ""); gsub(/MediaMountedReceiver;->b:Ljava\/lang\/Object;/, "MediaMountedReceiver;->b:Lht0;"); if (length($0)) print > file }
' "$work/smali/$class.smali"

{
    echo "build-tools $build_tools_version"
    echo "platform android-$platform_version"
    echo "javac $javac_version"
    echo "baksmali $(baksmali --version | head -n 1)"
    ( cd "$here" && sha256sum "$class.java" stub/*.java )
    ( cd "$out" && sha256sum *.smali )
} > "$out/provenance.txt"

if $verify; then
    for built in "$out"/*.smali; do
        cmp -s "$resources/$(basename "$built")" "$built" ||
            die "$(basename "$built") does not match its sources; run patches/restore/musicolet/generate.sh"
    done
    cmp -s <(hashes "$resources/provenance.txt") <(hashes "$out/provenance.txt") ||
        die "provenance.txt does not match its sources; run patches/restore/musicolet/generate.sh"
    echo "checked-in smali matches its sources"
fi
