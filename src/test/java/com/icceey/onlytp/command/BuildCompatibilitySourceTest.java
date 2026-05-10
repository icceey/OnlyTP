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
                "Forward-compatible fallback must stay limited to the Minecraft 1.21 series"
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
}
