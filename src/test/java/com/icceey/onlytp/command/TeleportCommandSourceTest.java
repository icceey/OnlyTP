package com.icceey.onlytp.command;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeleportCommandSourceTest {
    @Test
    void ridingEntityTeleportUsesReturnedCrossDimensionEntityForRemounting() throws IOException {
        String source = Files.readString(Path.of("src/main/java/com/icceey/onlytp/command/TeleportCommand.java"));

        assertTrue(
                source.matches("(?s).*\\.changeDimension\\s*\\(\\s*targetLevel\\s*,.*"),
                "Cross-dimension riding entity teleport must use Forge's returned replacement entity"
        );
        assertFalse(
                source.contains("startRiding(livingRidingEntity"),
                "Player must not remount the stale pre-dimension-change riding entity reference"
        );
    }
}
