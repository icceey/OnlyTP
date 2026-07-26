package com.icceey.onlytp.compat;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;

import java.util.Set;

public final class MinecraftCompatImpl extends LegacyMinecraftCompatBase {
    @Override
    public ServerLevel getServerLevel(Entity entity) {
        return (ServerLevel) entity.level();
    }

    @Override
    public boolean teleportEntityTo(Entity entity, ServerLevel targetLevel,
                                    double targetX, double targetY, double targetZ,
                                    float targetYRot, float targetXRot, boolean setCamera) {
        if (entity instanceof ServerPlayer player) {
            player.teleportTo(targetLevel, targetX, targetY, targetZ, targetYRot, targetXRot);
            return true;
        }

        return entity.teleportTo(
                targetLevel,
                targetX,
                targetY,
                targetZ,
                Set.<RelativeMovement>of(),
                targetYRot,
                targetXRot
        );
    }

    @Override
    public Component translatableWithFallback(String key, Object... args) {
        String serverText = Component.translatable(key).getString();
        return Component.translatableWithFallback(key, serverText, args);
    }

    @Override
    public void sendSuccess(CommandSourceStack source, Component message, boolean broadcastToAdmins) {
        source.sendSuccess(() -> message, broadcastToAdmins);
    }

    @Override
    public void sendSystemMessage(ServerPlayer player, Component message) {
        player.sendSystemMessage(message);
    }
}
