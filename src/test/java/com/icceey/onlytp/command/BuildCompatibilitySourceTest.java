package com.icceey.onlytp.command;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildCompatibilitySourceTest {
    @Test
    void futureMinecraft121PatchesUseLatestKnownCompatibilityLayer() throws IOException {
        String buildScript = Files.readString(Path.of("build.gradle"));

        assertTrue(
                buildScript.contains("minecraftPatchVersion >= 11"),
                "Minecraft 1.21.12+ should optimistically use the latest known 1.21.11 compatibility layer"
        );
        assertTrue(
                buildScript.contains("targetMinecraftVersionParts[0] == 1")
                        && buildScript.contains("targetMinecraftVersionParts[1] == 21"),
                "Minecraft 1.21 compatibility detection must remain explicit"
        );
        assertTrue(
                buildScript.contains("compatSourceSet = 'compat_1_21_11'"),
                "The latest known 1.21 compatibility layer should remain the fallback for newer 1.21 patches"
        );
        assertFalse(
                buildScript.contains("targetMinecraftVersion == '1.21.11'"),
                "Build compatibility selection should not hard-code only the last known patch version"
        );
    }

    @Test
    void minecraft26UsesItsOwnCompatibilityLayerAndJava25() throws IOException {
        String buildScript = Files.readString(Path.of("build.gradle"));

        assertTrue(
                buildScript.contains("targetMinecraftVersionParts[0] == 26"),
                "Minecraft 26.x targets must be recognized"
        );
        assertTrue(
                buildScript.contains("compatSourceSet = 'compat_26_1'"),
                "Minecraft 26.x targets must use the latest known 26.x compatibility layer"
        );
        assertTrue(
                buildScript.contains("ext.targetJavaVersion = isMinecraft26 ? 25 : 21"),
                "Minecraft 26.x must use Java 25 while Minecraft 1.21.x remains on Java 21"
        );
        assertTrue(
                Files.exists(Path.of("common/src/compat_26_1/java/com/icceey/onlytp/compat/MinecraftCompatImpl.java")),
                "Minecraft 26.x compatibility implementation must exist"
        );
        assertFalse(
                buildScript.contains("targetMinecraftVersion == '26.1'")
                        || buildScript.contains("targetMinecraftVersion == '26.2'"),
                "Minecraft 26.x compatibility selection should not hard-code individual releases"
        );
    }

    @Test
    void bothLoadersCompileTheSameCommonAndVersionSpecificSources() throws IOException {
        String neoForgeBuild = Files.readString(Path.of("neoforge/build.gradle"));
        String fabricBuild = Files.readString(Path.of("fabric/build.gradle"));

        assertTrue(
                neoForgeBuild.contains("common/src/main/java")
                        && fabricBuild.contains("common/src/main/java"),
                "Both loader builds must compile the shared command sources"
        );
        assertTrue(
                neoForgeBuild.contains("rootProject.compatSourceDir")
                        && fabricBuild.contains("rootProject.compatSourceDir"),
                "Both loader builds must compile the selected Minecraft compatibility layer"
        );
        assertTrue(
                fabricBuild.contains("net.fabricmc.fabric-loom-remap")
                        && fabricBuild.contains("net.fabricmc.fabric-loom"),
                "Fabric must select the Loom variant matching obfuscated 1.21.x or unobfuscated 26.x"
        );
    }

    @Test
    void legacyForgeTargetsUseAnIsolatedPinnedBuild() throws IOException {
        String settings = Files.readString(Path.of("legacy-forge/settings.gradle"));
        String buildScript = Files.readString(Path.of("legacy-forge/build.gradle"));
        String properties = Files.readString(Path.of("legacy-forge/gradle.properties"));
        String wrapper = Files.readString(Path.of("legacy-forge/gradle/wrapper/gradle-wrapper.properties"));

        assertTrue(
                settings.contains("'forge-1.18.2', 'forge-1.19.2', 'forge-1.20.1'"),
                "Legacy Forge must keep exactly the three frozen Minecraft target projects"
        );
        assertTrue(
                properties.contains("forge_1_18_2_version=40.3.12")
                        && properties.contains("forge_1_19_2_version=43.5.2")
                        && properties.contains("forge_1_20_1_version=47.4.22"),
                "Legacy Forge dependencies must be explicitly pinned"
        );
        assertTrue(
                buildScript.contains("JavaLanguageVersion.of(17)")
                        && buildScript.contains("net.minecraftforge.gradle")
                        && buildScript.contains("../common/src/main/java")
                        && buildScript.contains("../common/src/main/resources"),
                "Legacy Forge must use Java 17 and share loader-neutral command code and resources"
        );
        assertTrue(
                wrapper.contains("gradle-8.14.4-bin.zip"),
                "Legacy Forge must stay isolated from the modern Gradle 9 wrapper"
        );
        assertTrue(
                Files.isRegularFile(Path.of("legacy-forge/gradle/wrapper/gradle-wrapper.jar"))
                        && Files.isRegularFile(Path.of("legacy-forge/gradlew"))
                        && Files.isRegularFile(Path.of("legacy-forge/gradlew.bat")),
                "Legacy Forge must include a complete standalone Gradle wrapper"
        );
    }

    @Test
    void legacyForgeLocalizationUsesClientTranslationsWhenOnlyTpIsInstalled() throws IOException {
        String base = Files.readString(Path.of(
                "legacy-forge/src/main/java/com/icceey/onlytp/compat/LegacyMinecraftCompatBase.java"
        ));
        String forge1182 = Files.readString(Path.of(
                "legacy-forge/src/compat_1_18_2/java/com/icceey/onlytp/compat/MinecraftCompatImpl.java"
        ));
        String forge1192 = Files.readString(Path.of(
                "legacy-forge/src/compat_1_19_2/java/com/icceey/onlytp/compat/MinecraftCompatImpl.java"
        ));

        assertTrue(
                base.contains("connection.isMemoryConnection()")
                        && base.contains("NetworkHooks.getConnectionData(connection)")
                        && base.contains("connectionData.getModList().contains(OnlyTPForge.MODID)"),
                "Legacy Forge must recognize integrated clients and use handshake data for remote clients"
        );
        assertTrue(
                forge1182.contains("clientHasOnlyTP(recipient)")
                        && forge1182.contains("new TranslatableComponent(key, args)")
                        && forge1182.contains("new TextComponent(englishFallback(key, args))"),
                "Forge 1.18.2 must use client translations when available and readable fallback otherwise"
        );
        assertTrue(
                forge1192.contains("clientHasOnlyTP(recipient)")
                        && forge1192.contains("Component.translatable(key, args)")
                        && forge1192.contains("Component.literal(englishFallback(key, args))"),
                "Forge 1.19.2 must use client translations when available and readable fallback otherwise"
        );
    }

    @Test
    void fabricRunDirectoriesResolveToTheRootRunDirectory() throws IOException {
        String fabricBuild = Files.readString(Path.of("fabric/build.gradle"));

        assertTrue(
                fabricBuild.contains("runDir = '../run/fabric/client'")
                        && fabricBuild.contains("runDir = '../run/fabric/server'"),
                "Fabric run directories must resolve from the Fabric subproject to the root run directory"
        );
        assertFalse(
                fabricBuild.contains("rootProject.file('run/fabric/"),
                "Loom must not receive absolute run directory strings that it resolves relative to the Fabric project"
        );
    }
}
