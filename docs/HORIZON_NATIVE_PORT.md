# Horizon native mod port plan

## Correct package format

The intended in-game deliverable is a Horizon **`.hnmod`** native mod, not a
`.jar` and not a content-only `.hmod`.

A native bundle is a ZIP containing:
- `horizon-native.json`
- `horizon-mixin.toml`
- Rust module and hook helper `.rs` files referenced by the manifest

The launcher compiles the bundle with Horizon's native-mod toolchain. It is
not a JVM plugin loader. Fabric/Forge/NeoForge JARs do not execute in Horizon.

## Why the existing JAR is not convertible

Geyser's bridge is not just a LAN discovery hook. It includes:
- Bedrock UDP/RakNet session handling and Cloudburst Bedrock protocol;
- Bedrock-to-Java packet translation;
- Java protocol connection and session handling;
- entity, inventory, movement, world, and configuration translation.

Those parts are implemented in Java and rely on Java libraries. A valid
`.hnmod` cannot load that JAR as a runtime dependency. Renaming the file or
wrapping it in the native bundle would only create an invalid/nonfunctional
package.

## Port architecture required

1. Keep Geyser as the protocol reference, but implement the Bedrock/RakNet
   transport and Bedrock protocol handling in Rust (or identify a compatible
   Rust implementation and verify its protocol version).
2. Integrate with Horizon's actual LAN server connection and lifecycle using
   supported native hooks. Do not guess ports; obtain the bound listener
   address from the server or its LAN announcement.
3. Translate Bedrock packets to Horizon's Java-compatible server protocol and
   map login, configuration, play, movement, chunks, entities, inventory,
   chat, and disconnect flows.
4. Build a valid native bundle against a pinned Horizon revision and the
   matching `horizon-mixins` revision.
5. Run `horizon-launcher native-build <bundle> --trust-native-code` and the
   Horizon native-mod checker, then test an actual Bedrock client against a
   hosted world.

## Current repository state

- [x] Geyser Standalone compiles as a separate Java process.
- [x] Prototype discovery of Horizon's Open-to-LAN multicast announcement.
- [ ] Rust Bedrock/RakNet implementation.
- [ ] Horizon native hooks and server lifecycle integration.
- [ ] Valid `.hnmod` package.
- [ ] End-to-end Bedrock login and gameplay tests.

The standalone JAR is retained as a protocol/reference prototype only. It is
not the requested final deliverable.
