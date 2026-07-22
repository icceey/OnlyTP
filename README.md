# OnlyTP

[English](README.md) | [中文](README_zh.md)

OnlyTP is a lightweight Minecraft NeoForge and Fabric mod that enhances your gameplay with convenient teleportation features. Whether you're exploring vast worlds or helping friends in need, OnlyTP makes traveling across your Minecraft server simple and efficient.

## ✨ Features

- **🎯 Simple Commands**: Intuitive teleportation with easy-to-remember commands
- **🌟 Enhanced Experience**: Teleport with elegant particle effects and sound feedback
- **⚡ Multi-loader Support**: Native builds for NeoForge and Fabric from one shared codebase

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

- **Minecraft Versions**: 1.21 through 1.21.11, plus 26.1 through 26.2
- **Loader**: NeoForge or Fabric Loader; use the jar matching both your loader and Minecraft version
- **Java**: Java 21 for Minecraft 1.21.x; Java 25 for Minecraft 26.x
- **Dependencies**: Fabric builds require Fabric API; NeoForge builds have no additional mod dependency
- **Installation**: Server-side required, client-side optional
  - Install on client for localized messages
  - Server-only installation uses server language for all players

## 🛠️ Development

The `master` branch contains both loader implementations:

- `common/` - shared command logic, resources, and Minecraft-version compatibility layers
- `neoforge/` - NeoForge entry point and metadata
- `fabric/` - Fabric entry point and metadata

Build both distributable jars for the selected Minecraft version with:

```bash
./gradlew build
```

The jars are written to `neoforge/build/libs/` and `fabric/build/libs/`.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.
