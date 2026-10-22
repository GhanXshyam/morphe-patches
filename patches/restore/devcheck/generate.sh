#!/usr/bin/env bash
set -euo pipefail

here=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
resources=$here/../../src/main/resources/devcheck/restored

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

"$javac" -nowarn -source 8 -target 8 -d "$work/classes" -cp "$platform" "$here"/stub/*.java "$here"/DevCheckRestored.java
"$build_tools/d8" --release --min-api 23 --lib "$platform" --classpath "$work/classes" --output "$work" "$work"/classes/DevCheckRestored*.class
baksmali d --sequential-labels -o "$work/smali" "$work/classes.dex"
for smali in "$work"/smali/DevCheckRestored*.smali; do
    sed -E '/^\s*\.(source|line) /d' "$smali" > "$out/$(basename "$smali")"
done

{
    echo "build-tools $build_tools_version"
    echo "platform android-$platform_version"
    echo "javac $javac_version"
    echo "baksmali $(baksmali --version | head -n 1)"
    ( cd "$here" && sha256sum DevCheckRestored.java stub/*.java )
    ( cd "$out" && sha256sum DevCheckRestored*.smali )
} > "$out/provenance.txt"

if $verify; then
    for built in "$out"/*.smali; do
        cmp -s "$resources/$(basename "$built")" "$built" ||
            die "$(basename "$built") does not match its sources; run patches/restore/devcheck/generate.sh"
    done
    cmp -s <(hashes "$resources/provenance.txt") <(hashes "$out/provenance.txt") ||
        die "provenance.txt does not match its sources; run patches/restore/devcheck/generate.sh"
    echo "checked-in smali matches its sources"
fi
