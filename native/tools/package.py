#!/usr/bin/env python3
"""Create a deterministic source-only Horizon .hnmod bundle."""
import hashlib
import json
from pathlib import Path
import tomllib
import zipfile

ROOT = Path(__file__).resolve().parents[1]
GAME = "26.3"

def main():
    compatibility = json.loads((ROOT / "compatibility.json").read_text())
    manifest_path = ROOT / "mixins/geyser_bridge/horizon-mixin.toml"
    mixin = tomllib.loads(manifest_path.read_text())
    mod = mixin["mod"]
    if mod["id"] != "geyser_bridge":
        raise SystemExit("bundle folder and mod id must match")
    if mixin["compatibility"]["horizon_revision"] != compatibility["horizon_revision"]:
        raise SystemExit("Horizon revision mismatch")
    native_manifest = {
        "schema": 1, "id": mod["id"], "version": mod["version"], "game": GAME,
        "horizon": compatibility["horizon_revision"],
        "mixins": compatibility["horizon_mixins_revision"],
    }
    files = {"horizon-native.json": (json.dumps(native_manifest, indent=2, sort_keys=True) + "\n").encode()}
    bundle = ROOT / "mixins/geyser_bridge"
    for path in sorted(bundle.iterdir()):
        if path.is_file() and path.suffix in (".rs", ".toml"):
            files[path.relative_to(ROOT).as_posix()] = path.read_bytes()
    output_name = "{}-{}.hnmod".format(mod["id"], mod["version"])
    output = ROOT / "dist" / output_name
    output.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(output, "w", compression=zipfile.ZIP_DEFLATED) as archive:
        for name, data in sorted(files.items()):
            info = zipfile.ZipInfo(name, (2026, 1, 1, 0, 0, 0))
            info.compress_type = zipfile.ZIP_DEFLATED
            info.external_attr = 0o100644 << 16
            archive.writestr(info, data)
    digest = hashlib.sha256(output.read_bytes()).hexdigest()
    print(json.dumps({"file": str(output), "size": output.stat().st_size, "sha256": digest}, indent=2))

if __name__ == "__main__":
    main()
