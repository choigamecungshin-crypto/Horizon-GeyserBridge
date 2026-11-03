# Horizon native bundle

This folder is the first native bundle for Horizon-GeyserBridge. It pins Horizon commit
c076902dd5530a0c947107bed7244e59387f81f9 and horizon-mixins commit
ddd9700b21997c95d95ac6ee5ecafe3830cf622a.

The current hook attaches to Horizon authoritative server startup. It does NOT
accept Bedrock connections or translate packets. The standalone Geyser JAR is
a protocol reference only and is not loaded by this bundle.

## Package layout

- horizon-native.json at archive root
- mixins/geyser_bridge/horizon-mixin.toml
- mixins/geyser_bridge/*.rs

A package is valid only after it is staged and compiled against the pinned
Horizon source with the matching horizon-mixins tool. Renaming a JAR does not
produce a native mod.

## Remaining implementation

- [x] Pin Horizon and horizon-mixins revisions.
- [x] Add a native server-start hook.
- [ ] Implement reliable LAN announcement discovery in the native runtime.
- [ ] Implement Bedrock UDP/RakNet transport.
- [ ] Implement Bedrock protocol login and packet translation.
- [ ] Connect translated sessions to Horizon Java-compatible LAN server.
- [ ] Build and test the bundle with the Horizon launcher.
- [ ] Test a real Bedrock client joining an Open-to-LAN world.

The hook is a compile-time integration checkpoint, not a claim that Bedrock
multiplayer is working.