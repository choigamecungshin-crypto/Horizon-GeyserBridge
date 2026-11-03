<img src="https://geysermc.org/img/geyser-1760-860.png" alt="Geyser" width="600"/>

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Discord](https://img.shields.io/discord/613163671870242838.svg?color=%237289da&label=discord)](https://discord.gg/geysermc)
[![Crowdin](https://badges.crowdin.net/e/51361b7f8a01644a238d0fe8f3bddc62/localized.svg)](https://translate.geysermc.org/)

Geyser is a bridge between Minecraft: Bedrock Edition and Minecraft: Java Edition, closing the gap from those wanting to play true cross-platform.

Geyser is an [Open Collaboration](https://opencollaboration.dev/) project.

## What is Geyser?
Geyser is a proxy, bridging the gap between Minecraft: Bedrock Edition and Minecraft: Java Edition servers.
The ultimate goal of this project is to allow Minecraft: Bedrock Edition users to join Minecraft: Java Edition servers as seamlessly as possible. However, due to the nature of Geyser translating packets over the network of two different games, *do not expect everything to work perfectly!*

Special thanks to the DragonProxy project for being a trailblazer in protocol translation and for all the team members who have joined us here!

## Supported Versions

| Edition | Supported Versions                                                                                |
|---------|---------------------------------------------------------------------------------------------------|
| Bedrock | 26.40, 26.41, 26.42, 26.43, 26.44, 26.45, 26.50, 26.51, 26.52, 26.60                              |
| Java    | 26.3 (For older versions, [see this guide](https://geysermc.org/wiki/geyser/supported-versions/)) |

## Setting Up
Take a look [here](https://geysermc.org/wiki/geyser/setup/) for how to set up Geyser.

## Links:
- Website: https://geysermc.org
- Docs: https://geysermc.org/wiki/geyser/
- Download: https://geysermc.org/download
- Discord: https://discord.gg/geysermc
- Donate: https://opencollective.com/geysermc
- Test Server: `test.geysermc.org` port `25565` for Java and `19132` for Bedrock

## What's Left to be Added/Fixed
- Near-perfect movement (to the point where anticheat on large servers is unlikely to ban you)
- Some Entity Flags

## What can't be fixed
There are a few things Geyser is unable to support due to various differences between Minecraft Bedrock and Java. For a list of these limitations, see the [Current Limitations](https://geysermc.org/wiki/geyser/current-limitations/) page.

## Compiling
1. Clone the repo to your computer
2. Navigate to the Geyser root directory and run `git submodule update --init --recursive`. This command downloads all the needed submodules for Geyser and is a crucial step in this process.
3. Run `gradlew build` and locate to `bootstrap/build` folder.

## Contributing
Any contributions are appreciated. Please feel free to reach out to us on [Discord](https://discord.gg/geysermc) if
you're interested in helping out with Geyser.

## Libraries Used:
- [Adventure Text Library](https://github.com/KyoriPowered/adventure)
- [CloudburstMC Bedrock Protocol Library](https://github.com/CloudburstMC/Protocol)
- [GeyserMC's Java Protocol Library](https://github.com/GeyserMC/MCProtocolLib)
- [TerminalConsoleAppender](https://github.com/Minecrell/TerminalConsoleAppender)
- [Simple Logging Facade for Java (slf4j)](https://github.com/qos-ch/slf4j)


## Horizon integration status

**This repository does not yet produce a Horizon mod.** The current CI artifact is a
Java Geyser Standalone JAR, which must run as a separate process. It is a
network-bridge prototype, not an installable Horizon mod.

Horizon uses two different mod formats:

- `.hmod`: content/assets only; it cannot run Geyser.
- `.hnmod`: native Rust code bundled with `horizon-mixin.toml` and
  `horizon-native.json`. The Horizon launcher builds that source into a game
  instance. Renaming a JAR or putting a JAR inside this ZIP does not make it a
  valid `.hnmod`.

Geyser's current implementation is Java and depends on its Java protocol stack
and Cloudburst's Bedrock/RakNet libraries. A real `.hnmod` therefore requires
a Rust-native port or a separately supported IPC/proxy architecture; the
existing JAR cannot be embedded directly into Horizon's native mod loader.

### Existing standalone prototype

The current branch adds a standalone discovery mode for Horizon's integrated
server. It listens for the vanilla-compatible Java LAN announcement on
`224.0.2.60:4445/UDP`, reads the announced TCP port, and configures Geyser's
Java remote endpoint before Geyser starts.

1. Build with `./gradlew :standalone:shadowJar`.
2. Start the separate process on the same LAN:
   `java -jar Geyser-Standalone.jar --nogui --horizon-lan`.
3. Open the single-player world in Horizon and choose **Open to LAN**.
4. Connect Bedrock clients to the Geyser host's Bedrock UDP port (default
   `19132`).

This is only a prototype. The target is captured once at startup; reopening
LAN on a new port requires restarting Geyser. Authentication compatibility
depends on the Horizon world's mode and Geyser's remote configuration.
End-to-end Bedrock gameplay has not been validated.

### Required work for a real .hnmod

See [the native-port plan](docs/HORIZON_NATIVE_PORT.md). Until the Bedrock
protocol translation and Horizon-side integration exist in Rust, this project
must not label its JAR as a Horizon mod or claim that a Bedrock player can join
through an installed `.hnmod`.
