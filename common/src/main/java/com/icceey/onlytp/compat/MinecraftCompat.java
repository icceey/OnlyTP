package com.icceey.onlytp.compat;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface MinecraftCompat {
    boolean hasPermissionLevel(CommandSourceStack source, ServerPlayer player, int level);

    boolean teleportEntityTo(Entity entity, ServerLevel targetLevel,
                             double targetX, double targetY, double targetZ,
                             float targetYRot, float targetXRot, boolean setCamera);

    Entity teleportAcrossDimensions(LivingEntity entity, ServerLevel targetLevel,
                                    double targetX, double targetY, double targetZ,
                                    float targetYRot, float targetXRot);

    void startRiding(ServerPlayer player, Entity vehicle);
}
