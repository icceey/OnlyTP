# OnlyTP

[English](README.md) | [中文](README_zh.md)

OnlyTP is a lightweight Minecraft Forge, NeoForge, and Fabric mod that enhances your gameplay with convenient teleportation features. Whether you're exploring vast worlds or helping friends in need, OnlyTP makes traveling across your Minecraft server simple and efficient.

## ✨ Features

- **🎯 Simple Commands**: Intuitive teleportation with easy-to-remember commands
- **🌟 Enhanced Experience**: Teleport with elegant particle effects and sound feedback
- **⚡ Multi-loader Support**: Frozen legacy Forge builds plus actively maintained NeoForge and Fabric builds from one repository

## 🎮 Usage

### Basic Command
- **`/tlp <player>`**: Teleports you to the specified online player

### Features in Action
- **Particle Effects**: Beautiful portal particles appear at both departure and arrival locations
- **Sound Feedback**: Immersive portal sounds enhance the teleportation experience  
- **Player Notifications**: Both teleporter and target receive clear status messages

### 🔒 Safety Note
This mod is not recommended for competitive PvP servers as it may provide tactical advantages to players.

## 🔧 Compatibility

- **Legacy Forge**: Minecraft 1.18.2, 1.19.2, and 1.20.1; these Minecraft targets are frozen
- **Modern Builds**: Minecraft 1.21 through 1.21.11 and 26.1 through 26.2 on NeoForge or Fabric
- **Loader**: Use the jar matching both your loader and Minecraft version
- **Java**: Java 17 for legacy Forge, Java 21 for Minecraft 1.21.x, and Java 25 for Minecraft 26.x
- **Dependencies**: Fabric builds require Fabric API; Forge and NeoForge builds have no additional mod dependency
- **Installation**: Server-side required, client-side optional
  - Install on client for localized messages
  - Server-only installation keeps messages readable; Forge 1.18.2 and 1.19.2 use client translations when OnlyTP is installed and English fallback otherwise

## 🛠️ Development

The `master` branch contains every supported loader and Minecraft target:

- `common/` - loader-neutral command logic, resources, and modern Minecraft compatibility layers
- `neoforge/` - NeoForge entry point and metadata
- `fabric/` - Fabric entry point and metadata
- `legacy-forge/` - isolated Java 17/ForgeGradle build for the frozen 1.18.2, 1.19.2, and 1.20.1 targets

Build the NeoForge and Fabric jars for the selected modern Minecraft version with:

```bash
./gradlew build
```

Using a Java 17 environment, build all three frozen Forge targets with:

```bash
./legacy-forge/gradlew -p legacy-forge build
```

Modern jars are written to `neoforge/build/libs/` and `fabric/build/libs/`. Legacy jars are written to `legacy-forge/forge-<minecraft-version>/build/libs/`.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.
