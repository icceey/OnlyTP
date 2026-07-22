package com.icceey.onlytp.compat;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public final class MinecraftCompatImpl implements MinecraftCompat {
    @Override
    public boolean hasPermissionLevel(CommandSourceStack source, ServerPlayer player, int level) {
        return source.hasPermission(level);
    }

    @Override
    public boolean teleportEntityTo(Entity entity, ServerLevel targetLevel,
                                    double targetX, double targetY, double targetZ,
                                    float targetYRot, float targetXRot, boolean setCamera) {
        return entity.teleportTo(
                targetLevel,
                targetX,
                targetY,
                targetZ,
                Set.<Relative>of(),
                targetYRot,
                targetXRot,
                setCamera
        );
    }

    @Override
    public Entity teleportAcrossDimensions(LivingEntity entity, ServerLevel targetLevel,
                                           double targetX, double targetY, double targetZ,
                                           float targetYRot, float targetXRot) {
        return entity.teleport(new TeleportTransition(
                targetLevel,
                new Vec3(targetX, targetY, targetZ),
                entity.getDeltaMovement(),
                targetYRot,
                targetXRot,
                TeleportTransition.DO_NOTHING
        ));
    }

    @Override
    public void startRiding(ServerPlayer player, Entity vehicle) {
        player.startRiding(vehicle, true, false);
    }
}
