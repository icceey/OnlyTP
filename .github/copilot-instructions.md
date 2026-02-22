# OnlyTP — Copilot Instructions

## Project Overview
OnlyTP is a minimal Minecraft Forge mod (MC 1.20.1 / Forge 47.4.16) that adds a single `/tlp <player>` command allowing players to teleport to online players, with portal particle effects and sound feedback.

## Architecture
The codebase has exactly two Java source files:
- `src/main/java/com/icceey/onlytp/OnlyTP.java` — mod entry point (`@Mod("onlytp")`), registers `MinecraftForge.EVENT_BUS` listeners and command registration via `RegisterCommandsEvent`
- `src/main/java/com/icceey/onlytp/command/TeleportCommand.java` — full Brigadier command implementation

All new features (commands, events) follow this two-layer pattern: register in `OnlyTP.java`, implement in `command/` (or a new sub-package).

## Build & Run
```bash
./gradlew build           # outputs to build/libs/onlytp-forge-1.20.1-<version>.jar
./gradlew runClient       # launch MC client for manual testing (working dir: run/)
./gradlew runServer       # launch dedicated server for testing
./gradlew runData         # run data generators (produces src/generated/resources/)
```
`gradle.properties` is the single source of truth for versions (`mod_version`, `minecraft_version`, `forge_version`). The `mod_id` value (`onlytp`) must stay in sync between `gradle.properties`, `OnlyTP.MODID`, and `@Mod("onlytp")`.

## Command Pattern (Brigadier)
Commands are registered via `Commands.literal().argument().suggests().executes()`:
```java
dispatcher.register(Commands.literal("tlp")
    .then(Commands.argument("player", EntityArgument.player())
        .suggests(ONLINE_PLAYERS_SUGGESTION)
        .executes(TeleportCommand::execute)
    )
);
```
Custom `SuggestionProvider<CommandSourceStack>` filters the suggestion list (e.g., excludes the executor from player suggestions).

## i18n Pattern
All user-facing text uses translation keys under `commands.onlytp.*`. The `translatableWithFallback()` helper ensures messages display correctly even when the client has no lang file installed (server-only installs):
```java
private static Component translatableWithFallback(String key, Object... args) {
    String serverText = Component.translatable(key).getString();
    return Component.translatableWithFallback(key, serverText, args);
}
```
Lang files live in `src/main/resources/assets/onlytp/lang/` (en_us, zh_cn, zh_hk, zh_tw, ja_jp, lzh). **Every new message key must be added to all six files.**

## Key Implementation Conventions
- **Self-TP guard**: ops (permission level ≥ 2) may TP to themselves; regular players cannot.
- **Vehicle handling**: if executor is riding a `LivingEntity`, dismount → teleport the mount → teleport the player → remount. Do not skip this or the mount will be left behind.
- **Effects**: use `ParticleTypes.PORTAL` at departure and `ParticleTypes.REVERSE_PORTAL` at destination; play `SoundEvents.PORTAL_TRAVEL` at both locations with volume `0.1F`.
- **Event bus**: Forge game events use `MinecraftForge.EVENT_BUS`; mod lifecycle events use the mod-specific bus (`FMLJavaModLoadingContext`).

## Resources
- `src/main/resources/META-INF/mods.toml` — mod metadata; variables like `${mod_id}` are substituted from `gradle.properties` at build time via `processResources`.
- `src/main/resources/pack.mcmeta` — resource pack metadata required by Forge.
- `run/` — working directory for `runClient`/`runServer`; contains world saves, configs, and logs. Do not commit saves or logs.
