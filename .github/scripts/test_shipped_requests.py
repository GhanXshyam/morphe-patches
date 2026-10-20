#!/usr/bin/env python3
"""python3 .github/scripts/test_shipped_requests.py"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent))
from shipped_requests import shipped


def patches_list(*apps):
    return {"patches": [{"compatiblePackages": [{"name": name, "packageName": package}]} for name, package in apps]}


def request(number, app, package, state="OPEN"):
    body = f"### App\n\n{app}\n\n### Package name\n\n{package}\n\n### Requested features\n\nUnlock premium\n"
    return {"number": number, "state": state, "body": body}


PREVIOUS = patches_list(("Old App", "com.old.app"))
CURRENT = patches_list(("Old App", "com.old.app"), ("New App", "com.new.app"), ("Other App", "com.other.app"))
REQUESTS = [
    request(1, "New App", "com.new.app"),
    request(2, "New App again", "COM.New.App"),
    request(3, "Old App", "com.old.app"),
    request(4, "Closed", "com.other.app", state="CLOSED"),
    request(5, "Linked, https://play.google.com/store/apps/details?id=com.other.app", "_No response_"),
    request(6, "Unrelated", "com.unrelated.app"),
]

failures = 0


def check(ok, name):
    global failures
    failures += not ok
    print(f"{'ok  ' if ok else 'FAIL'} {name}")


rows = shipped(PREVIOUS, CURRENT, REQUESTS)
check([row["number"] for row in rows] == [1, 2, 5], "open requests naming a package new in this release are listed")
check(all(row["app"] == ("New App" if row["package"] == "com.new.app" else "Other App") for row in rows),
      "each row carries the app name from the patches list")
check(shipped(CURRENT, CURRENT, REQUESTS) == [], "a release that adds no package lists nothing")

print(f"\n{3 - failures}/3 passed")
sys.exit(1 if failures else 0)
