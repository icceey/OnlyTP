package com.icceey.onlytp.command;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeleportCommandSourceTest {
    @Test
    void ridingEntityTeleportUsesReturnedCrossDimensionEntityForRemounting() throws IOException {
        String commandSource = Files.readString(Path.of("common/src/main/java/com/icceey/onlytp/command/TeleportCommand.java"));
        String compatSources;
        try (Stream<Path> sources = readJavaSources(Path.of("common/src"))) {
            compatSources = sources
                    .filter(path -> path.toString().contains("/compat"))
                    .map(TeleportCommandSourceTest::readSourceUnchecked)
                    .collect(Collectors.joining("\n"));
        }
        String legacyForgeSources;
        try (Stream<Path> sources = readJavaSources(Path.of("legacy-forge/src"))) {
            legacyForgeSources = sources
                    .map(TeleportCommandSourceTest::readSourceUnchecked)
                    .collect(Collectors.joining("\n"));
        }

        assertTrue(
                compatSources.contains("net.minecraft.world.level.portal.TeleportTransition"),
                "Minecraft 1.21.2+ cross-dimension riding entity teleport must use the returned replacement entity"
        );
        assertTrue(
                compatSources.contains("net.minecraft.world.level.portal.DimensionTransition"),
                "Minecraft 1.21/1.21.1 cross-dimension riding entity teleport must keep the returned replacement entity path"
        );
        assertTrue(
                legacyForgeSources.contains("net.minecraftforge.common.util.ITeleporter")
                        && legacyForgeSources.contains("return entity.changeDimension("),
                "Legacy Forge cross-dimension riding entity teleport must use the returned ITeleporter replacement entity"
        );
        assertTrue(
                commandSource.contains("return teleportedEntity instanceof LivingEntity teleportedLivingEntity ? teleportedLivingEntity : null"),
                "Cross-dimension riding entity teleport must remount the returned replacement entity"
        );
        assertFalse(
                commandSource.contains("startRiding(livingRidingEntity"),
                "Player must not remount the stale pre-dimension-change riding entity reference"
        );
        assertFalse(
                commandSource.contains("java.lang.reflect"),
                "TeleportCommand should keep version compatibility details out of command flow"
        );
        assertFalse(
                commandSource.contains("Class.forName")
                        || commandSource.contains("getMethod(")
                        || commandSource.contains("invoke("),
                "TeleportCommand should not use reflection for Minecraft version compatibility"
        );
        assertFalse(
                commandSource.contains("net.minecraftforge") || compatSources.contains("net.minecraftforge"),
                "Modern shared teleport code must not contain Forge API imports"
        );
        assertTrue(
                commandSource.contains("COMPAT.getServerLevel(")
                        && commandSource.contains("COMPAT.sendSuccess(")
                        && commandSource.contains("COMPAT.sendSystemMessage(")
                        && commandSource.contains("COMPAT.translatableWithFallback("),
                "Minecraft-version-specific world and message APIs must stay behind the compatibility interface"
        );
    }

    @Test
    void loaderApisStayOutsideCommonSources() throws IOException {
        String commonSources;
        try (Stream<Path> sources = readJavaSources(Path.of("common/src"))) {
            commonSources = sources.map(TeleportCommandSourceTest::readSourceUnchecked)
                    .collect(Collectors.joining("\n"));
        }

        String neoForgeSources;
        try (Stream<Path> sources = readJavaSources(Path.of("neoforge/src"))) {
            neoForgeSources = sources.map(TeleportCommandSourceTest::readSourceUnchecked)
                    .collect(Collectors.joining("\n"));
        }

        String fabricSources;
        try (Stream<Path> sources = readJavaSources(Path.of("fabric/src"))) {
            fabricSources = sources.map(TeleportCommandSourceTest::readSourceUnchecked)
                    .collect(Collectors.joining("\n"));
        }

        String legacyForgeSources;
        try (Stream<Path> sources = readJavaSources(Path.of("legacy-forge/src"))) {
            legacyForgeSources = sources.map(TeleportCommandSourceTest::readSourceUnchecked)
                    .collect(Collectors.joining("\n"));
        }

        assertFalse(
                commonSources.contains("net.neoforged")
                        || commonSources.contains("net.fabricmc")
                        || commonSources.contains("net.minecraftforge"),
                "Common sources must remain loader-neutral"
        );
        assertFalse(neoForgeSources.contains("net.fabricmc"), "NeoForge sources must not import Fabric APIs");
        assertFalse(fabricSources.contains("net.neoforged"), "Fabric sources must not import NeoForge APIs");
        assertFalse(
                legacyForgeSources.contains("net.neoforged") || legacyForgeSources.contains("net.fabricmc"),
                "Legacy Forge sources must not import NeoForge or Fabric APIs"
        );
        assertTrue(
                fabricSources.contains("TeleportCommand.register(dispatcher)"),
                "Fabric must delegate command registration to the shared implementation"
        );
        assertTrue(
                legacyForgeSources.contains("TeleportCommand.register(event.getDispatcher())"),
                "Legacy Forge must delegate command registration to the shared implementation"
        );
    }

    private static Stream<Path> readJavaSources(Path root) throws IOException {
        return Files.walk(root).filter(path -> path.toString().endsWith(".java"));
    }

    private static String readSourceUnchecked(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }
}
