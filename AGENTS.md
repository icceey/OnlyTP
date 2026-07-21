# OnlyTP Agent Instructions

## Project Overview

OnlyTP is a minimal Minecraft NeoForge mod for the Minecraft 1.21 series and
Minecraft 26.x. It adds one command, `/tlp <player>`, allowing a player to
teleport to another online player with portal particles, portal sound feedback,
target notifications, and riding-entity preservation.

This project no longer supports Forge. Do not add ForgeGradle, `net.minecraftforge`
imports, `META-INF/mods.toml`, `pack.mcmeta`, or Forge version properties back
into the project.

## Current Stack

- Loader/build: NeoForge via `net.neoforged.moddev`
- Default local development target: Minecraft `1.21.1` and NeoForge `21.1.242+`
- CI/release matrix: Minecraft `1.21` through `1.21.11` and `26.1` through
  `26.2`, with matching NeoForge builds
- Java: 21 for Minecraft 1.21.x and 25 for Minecraft 26.x; `.java-version`
  keeps the default 1.21.1 workspace on Java 21, while Gradle toolchains select
  the target-specific JDK
- Gradle wrapper: Gradle 9.2.1
- Mappings: Parchment for Minecraft 1.21.1 by default; matrix builds can
  override or disable Parchment when a target has no Parchment release

`gradle.properties` is the single source of truth for `mod_id`, `mod_version`,
`minecraft_version`, and `neo_version`. Keep `mod_id=onlytp` synchronized with
`OnlyTP.MODID` and the `@Mod(OnlyTP.MODID)` annotation.

## Source Layout

Common Java sources live under `src/main/java`:

- `src/main/java/com/icceey/onlytp/OnlyTP.java` - NeoForge mod entry point.
  It registers `NeoForge.EVENT_BUS` listeners and command registration through
  `RegisterCommandsEvent`.
- `src/main/java/com/icceey/onlytp/command/TeleportCommand.java` - full
  Brigadier command implementation for `/tlp <player>`.
- `src/main/java/com/icceey/onlytp/compat/MinecraftCompat.java` - compatibility
  interface used by the command logic.

Version-specific Minecraft API calls live outside `src/main/java` and are
selected by `minecraft_version` in `build.gradle`:

- `src/compat_1_21_1/java/` - Minecraft `1.21` and `1.21.1`
- `src/compat_1_21_2/java/` - Minecraft `1.21.2` through `1.21.8`
- `src/compat_1_21_9/java/` - Minecraft `1.21.9` and `1.21.10`
- `src/compat_1_21_11/java/` - Minecraft `1.21.11` and newer `1.21.x`
  patches by default
- `src/compat_26_1/java/` - Minecraft `26.1` and newer `26.x` releases by
  default

Keep `TeleportCommand` free of reflection and version-branch details. Add or
adjust a compat source directory when Minecraft changes these command,
teleport, permission, or riding APIs again. The build intentionally treats
future patches in each supported version line optimistically: they reuse the
latest known compat layer until compilation or runtime testing proves an API
break.

Resources follow the NeoForge MDK layout:

- `src/main/resources/assets/onlytp/lang/*.json` - language files
- `src/main/templates/META-INF/neoforge.mods.toml` - generated mod metadata
  template

Do not recreate `src/main/resources/META-INF/`; the NeoForge ModDevGradle MDK
keeps loader metadata under `src/main/templates/META-INF/`.

## Build And Run

Use a fresh shell in the repository so jenv picks up `.java-version=21` for the
default Minecraft 1.21.1 target. Gradle selects or downloads Java 25 when a
Minecraft 26.x target is requested.

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

Matrix builds replace `1.21.1` with the target Minecraft version in the jar
name.

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
- Cross-dimension mount teleporting uses the version-appropriate transition API
  and remounts the replacement entity returned by the teleport operation, not
  the stale pre-teleport entity reference.

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
jar tf build/libs/onlytp-neoforge-*.jar | sort
```

The source-level regression test in `TeleportCommandSourceTest` intentionally
checks that Forge imports are absent and that cross-dimension riding-entity
teleporting uses the returned NeoForge/Minecraft replacement entity path.

## Runtime Testing Skill

When a change needs real in-game validation, first check whether the local
runtime testing skill is available at:

```text
~/.agents/skills/test-minecraft-mod-runtime/SKILL.md
```

If that skill is available, use it for singleplayer, multiplayer, Computer Use,
input-source handling, and Minecraft `1.21.x`/`26.x` matrix runtime testing. If
it is not available, ignore this note and continue with the smallest practical
manual verification for the requested change; do not fail a task only because
the skill is missing.

For OnlyTP-specific runtime checks, use `/tlp Alice` for singleplayer
self-teleport and `/tlp Bob` from Alice for two-client multiplayer checks. For
the riding path, create a mount in-game, ride it, then run the teleport command
and verify the player remains mounted.

## Maintenance Notes

- Keep GitHub Actions on JDK 21 for Minecraft 1.21.x and JDK 25 for Minecraft
  26.x.
- Keep Dependabot scoped to GitHub Actions updates unless the user asks for a
  broader ecosystem.
- The `forge.logging.markers` run property in `build.gradle` comes from the
  NeoForge MDK logging setup. Do not treat that single property name as Forge
  support.
