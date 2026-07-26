package com.icceey.onlytp.compat;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface MinecraftCompat {
    boolean hasPermissionLevel(CommandSourceStack source, ServerPlayer player, int level);

    ServerLevel getServerLevel(Entity entity);

    boolean teleportEntityTo(Entity entity, ServerLevel targetLevel,
                             double targetX, double targetY, double targetZ,
                             float targetYRot, float targetXRot, boolean setCamera);

    Entity teleportAcrossDimensions(LivingEntity entity, ServerLevel targetLevel,
                                    double targetX, double targetY, double targetZ,
                                    float targetYRot, float targetXRot);

    void startRiding(ServerPlayer player, Entity vehicle);

    Component translatableWithFallback(String key, Object... args);

    void sendSuccess(CommandSourceStack source, Component message, boolean broadcastToAdmins);

    void sendSystemMessage(ServerPlayer player, Component message);
}
