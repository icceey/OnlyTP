# OnlyTP Agent Instructions

## Project Overview

OnlyTP is a minimal Minecraft NeoForge mod for Minecraft 1.21.1 and NeoForge
21.1.228+. It adds one command, `/tlp <player>`, allowing a player to teleport
to another online player with portal particles, portal sound feedback, target
notifications, and riding-entity preservation.

This project no longer supports Forge. Do not add ForgeGradle, `net.minecraftforge`
imports, `META-INF/mods.toml`, `pack.mcmeta`, or Forge version properties back
into the project.

## Current Stack

- Loader/build: NeoForge via `net.neoforged.moddev`
- Minecraft: `1.21.1`
- NeoForge: `21.1.228+`
- Java: 21, enforced by `.java-version` and Gradle toolchains
- Gradle wrapper: Gradle 9.2.1
- Mappings: Parchment for Minecraft 1.21.1

`gradle.properties` is the single source of truth for `mod_id`, `mod_version`,
`minecraft_version`, and `neo_version`. Keep `mod_id=onlytp` synchronized with
`OnlyTP.MODID` and the `@Mod(OnlyTP.MODID)` annotation.

## Source Layout

The mod has two Java source files:

- `src/main/java/com/icceey/onlytp/OnlyTP.java` - NeoForge mod entry point.
  It registers `NeoForge.EVENT_BUS` listeners and command registration through
  `RegisterCommandsEvent`.
- `src/main/java/com/icceey/onlytp/command/TeleportCommand.java` - full
  Brigadier command implementation for `/tlp <player>`.

Resources follow the NeoForge MDK layout:

- `src/main/resources/assets/onlytp/lang/*.json` - language files
- `src/main/templates/META-INF/neoforge.mods.toml` - generated mod metadata
  template

Do not recreate `src/main/resources/META-INF/`; the NeoForge ModDevGradle MDK
keeps loader metadata under `src/main/templates/META-INF/`.

## Build And Run

Use a fresh shell in the repository so jenv picks up `.java-version=21`.

```bash
./gradlew build
./gradlew runClient
./gradlew runServer
./gradlew runData
```

`./gradlew build` outputs the distributable jar under:

```text
build/libs/onlytp-neoforge-1.21.1-<version>.jar
```

The `run/` directory is a local game working directory. Do not commit worlds,
logs, or runtime-generated game files.

## Command Behavior

Keep these behavior contracts intact unless the user explicitly asks for a
feature change:

- Only players can execute `/tlp`.
- Non-op players cannot teleport to themselves; permission level 2 or higher can.
- The target and executor must both be online and alive.
- Suggestions list online player names while excluding the executor.
- Teleport plays `SoundEvents.PORTAL_TRAVEL` at departure and destination.
- Departure particles use `ParticleTypes.PORTAL`.
- Destination particles use `ParticleTypes.REVERSE_PORTAL`.
- If the executor is riding a `LivingEntity`, dismount, teleport the mount,
  teleport the player, then remount the returned teleported mount entity.
- Cross-dimension mount teleporting uses `DimensionTransition` and remounts the
  replacement entity returned by `changeDimension(...)`, not the stale
  pre-teleport entity reference.

## Internationalization

All player-facing messages use translation keys under `commands.onlytp.*`.
`TeleportCommand.translatableWithFallback()` exists so server-only installs still
display readable messages when the client lacks this mod's lang files.

When adding or changing a message key, update every language file in:

```text
src/main/resources/assets/onlytp/lang/
```

Current language files:

- `en_us.json`
- `ja_jp.json`
- `lzh.json`
- `zh_cn.json`
- `zh_hk.json`
- `zh_tw.json`

## Testing And Verification

Before claiming a migration, behavior, or build change is complete, run the
smallest command that proves it. For normal changes, use:

```bash
./gradlew build --no-daemon --warning-mode all
```

Useful focused checks:

```bash
rg -n "net\\.minecraftforge|MinecraftForge|ForgeGradle|META-INF/mods\\.toml|pack\\.mcmeta|1\\.20\\.1|47\\." -g '!build/**' -g '!run/**'
jar tf build/libs/onlytp-neoforge-1.21.1-*.jar | sort
```

The source-level regression test in `TeleportCommandSourceTest` intentionally
checks that Forge imports are absent and that cross-dimension riding-entity
teleporting uses the returned NeoForge/Minecraft replacement entity path.

## Maintenance Notes

- Keep GitHub Actions on JDK 21.
- Keep Dependabot scoped to GitHub Actions updates unless the user asks for a
  broader ecosystem.
- The `forge.logging.markers` run property in `build.gradle` comes from the
  NeoForge MDK logging setup. Do not treat that single property name as Forge
  support.
