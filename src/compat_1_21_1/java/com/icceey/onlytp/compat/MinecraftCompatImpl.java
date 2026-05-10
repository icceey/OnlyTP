package com.icceey.onlytp.compat;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.portal.DimensionTransition;
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
    public Entity teleportAcrossDimensions(LivingEntity entity, ServerLevel targetLevel,
                                           double targetX, double targetY, double targetZ,
                                           float targetYRot, float targetXRot) {
        return entity.changeDimension(new DimensionTransition(
                targetLevel,
                new Vec3(targetX, targetY, targetZ),
                entity.getDeltaMovement(),
                targetYRot,
                targetXRot,
                DimensionTransition.DO_NOTHING
        ));
    }

    @Override
    public void startRiding(ServerPlayer player, Entity vehicle) {
        player.startRiding(vehicle, true);
    }
}
