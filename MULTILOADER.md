# OnlyTP Multi-Loader Development

This mod now supports both Forge and NeoForge development on the same Minecraft version (1.20.1) from a single codebase.

## Project Structure

```
OnlyTP/
├── src/
│   ├── main/
│   │   ├── java/               # Shared code (commands, utilities)
│   │   └── resources/          # Shared resources (assets, pack.mcmeta)
│   ├── forge/
│   │   ├── java/               # Forge-specific main class
│   │   └── resources/          # Forge mods.toml
│   └── neoforge/
│       ├── java/               # NeoForge-specific main class
│       └── resources/          # NeoForge mods.toml
├── build.gradle                # Multi-loader build configuration
└── gradle.properties           # Shared version properties
```

## Building

### Build for Forge
```bash
./gradlew clean build -PbuildTarget=forge
```
Output: `build/libs/onlytp-forge-1.20.1-1.0.0.jar`

### Build for NeoForge
```bash
./gradlew clean build -PbuildTarget=neoforge
```
Output: `build/libs/onlytp-neoforge-1.20.1-1.0.0.jar`

### Build for Both Platforms
```bash
./gradlew buildAll
```

## Development

### IDE Setup
1. Import the project into your IDE
2. Set the `buildTarget` project property to either `forge` or `neoforge`
3. Refresh Gradle project to apply the correct platform configuration

### Running in Development
```bash
# Forge client
./gradlew runClient -PbuildTarget=forge

# NeoForge client
./gradlew runClient -PbuildTarget=neoforge

# Forge server
./gradlew runServer -PbuildTarget=forge

# NeoForge server
./gradlew runServer -PbuildTarget=neoforge
```

## Code Organization

### Shared Code
- **Commands**: All commands are shared between platforms (`src/main/java/com/icceey/onlytp/command/`)
- **Utilities**: Common utility classes
- **Resources**: Language files, textures, models

### Platform-Specific Code
- **Main Class**: Each platform has its own mod entry point with platform-specific event bus registration
- **Metadata**: Platform-specific `mods.toml` with correct dependency declarations

### Adding New Features
1. **Shared functionality**: Add to `src/main/java/`
2. **Platform-specific features**: Add to `src/forge/java/` or `src/neoforge/java/`
3. **Resources**: Usually go in `src/main/resources/` unless platform-specific

## Version Configuration

Edit `gradle.properties` to update versions:
```properties
minecraft_version=1.20.1
forge_version=47.4.0
neoforge_version=47.1.106
mod_version=1.0.0
```

## Dependencies

The build system automatically uses the correct dependencies based on the build target:
- **Forge**: `net.minecraftforge:forge:1.20.1-47.4.0`
- **NeoForge**: `net.neoforged:neoforge:47.1.106`

## Continuous Integration

For CI/CD pipelines, build both versions:
```yaml
- name: Build Forge
  run: ./gradlew clean build -PbuildTarget=forge

- name: Build NeoForge  
  run: ./gradlew clean build -PbuildTarget=neoforge
```

## Publishing

The mod can be published to both Forge and NeoForge platforms:
1. Build both versions using the commands above
2. Upload the respective JAR files to CurseForge/Modrinth with platform-specific tags
3. Use the same mod version but different file names to distinguish platforms