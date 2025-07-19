# OnlyTP

A Minecraft mod that provides enhanced teleportation functionality. Easily teleport to your friends to help them.

**Multi-Loader Support**: This mod now supports both Forge and NeoForge from the same codebase!

## Features

- **Simple Commands**: Easy-to-use commands for teleporting players
- **No Dependencies**: This mod does not require any additional libraries or mods to function
- **Multi-Platform**: Available for both Forge and NeoForge

## Usage

Run the following commands in-game:
- `/tlp <player>`: Teleports you to the specified player.

**Note**: This mod is not recommended for use on PvP servers as it may provide unfair advantages.

## Compatibility

- **Minecraft Version**: 1.20.1
- **Forge Version**: 47.4.0+
- **NeoForge Version**: 47.1.106+
- **Side**: Only server is required, client is optional.
  - Install on client for localized messages, otherwise server language will be used.

## Development

This project uses a multi-loader architecture that supports both Forge and NeoForge development:

### Quick Build
```bash
# Build for both platforms
./build.sh

# Build for specific platform
./build.sh forge
./build.sh neoforge
```

### Detailed Documentation
See [MULTILOADER.md](MULTILOADER.md) for comprehensive development documentation.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.
