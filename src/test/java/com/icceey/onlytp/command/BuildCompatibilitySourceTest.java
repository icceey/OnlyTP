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
                buildScript.contains("compatSourceDir = 'src/compat_1_21_11/java'"),
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
                buildScript.contains("compatSourceDir = 'src/compat_26_1/java'"),
                "Minecraft 26.x targets must use the latest known 26.x compatibility layer"
        );
        assertTrue(
                buildScript.contains("targetJavaVersion = isMinecraft26 ? 25 : 21"),
                "Minecraft 26.x must use Java 25 while Minecraft 1.21.x remains on Java 21"
        );
        assertTrue(
                Files.exists(Path.of("src/compat_26_1/java/com/icceey/onlytp/compat/MinecraftCompatImpl.java")),
                "Minecraft 26.x compatibility implementation must exist"
        );
        assertFalse(
                buildScript.contains("targetMinecraftVersion == '26.1'")
                        || buildScript.contains("targetMinecraftVersion == '26.2'"),
                "Minecraft 26.x compatibility selection should not hard-code individual releases"
        );
    }
}
