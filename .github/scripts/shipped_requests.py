#!/usr/bin/env python3
"""python3 shipped_requests.py <previous-patches-list.json> <patches-list.json> <requests.json>"""

import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "actions" / "triage"))
from app_request import find_package


def packages(patches_list):
    return {
        package["packageName"].lower(): package["name"]
        for patch in patches_list["patches"]
        for package in patch.get("compatiblePackages") or []
    }


def shipped(previous, current, requests):
    added = {package: name for package, name in packages(current).items() if package not in packages(previous)}
    rows = []
    for request in requests:
        if request.get("state") != "OPEN":
            continue
        package = find_package(request.get("body") or "").lower()
        if package in added:
            rows.append({"number": request["number"], "package": package, "app": added[package]})
    return rows


if __name__ == "__main__":
    previous, current, requests = (json.loads(Path(path).read_text(encoding="utf-8")) for path in sys.argv[1:4])
    for row in shipped(previous, current, requests):
        print(json.dumps(row))
