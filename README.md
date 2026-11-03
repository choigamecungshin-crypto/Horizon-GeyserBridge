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


## Horizon Open-to-LAN bridge

This fork includes a standalone discovery mode for Horizon's integrated server.
It listens for Horizon's vanilla-compatible Java LAN announcement on
`224.0.2.60:4445/UDP`, reads the announced TCP port, and configures Geyser's
existing Java remote endpoint before Geyser starts. The port is discovered from
the announcement; it is not assumed to be `25565` and no port scan is used.

1. Build the standalone artifact with the normal Geyser build:
   `./gradlew :standalone:shadowJar`.
2. Start the resulting standalone JAR on the same LAN as the Horizon host:
   `java -jar Geyser-Standalone.jar --nogui --horizon-lan`.
3. Open the single-player world in Horizon and choose **Open to LAN**.
4. Connect Bedrock clients to the machine running Geyser on the configured
   Bedrock UDP port (default `19132`).

The discovery option waits until a Horizon world is published, then starts
Geyser against that exact host address and announced TCP port. Keep Geyser on
the same local network as Horizon; multicast discovery is not routed across
subnets by default.

### Current integration boundary

This mode reuses Geyser's existing Bedrock/RakNet listener, Java protocol
translation, and configured authentication. It does not patch Horizon or
change Horizon's host authentication policy. Set Geyser's `remote` Java
authentication mode appropriately for the Horizon host (online-mode worlds
require valid Java authentication; an offline-name host requires an offline
Geyser setup). Floodgate does not turn a Bedrock XUID into a Mojang Java
account.

The selected target is captured when Geyser starts. If the host closes and
reopens LAN on a different port, restart Geyser with `--horizon-lan` to
rediscover it. Automatic live retargeting, in-process Horizon integration,
and end-to-end gameplay validation remain outstanding.
