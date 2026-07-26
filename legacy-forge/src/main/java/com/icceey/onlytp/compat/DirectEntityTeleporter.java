package com.icceey.onlytp.compat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

record DirectEntityTeleporter(double x, double y, double z, float yRot, float xRot,
                              Vec3 deltaMovement) implements ITeleporter {
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel targetLevel,
                                    Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return new PortalInfo(new Vec3(x, y, z), deltaMovement, yRot, xRot);
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentLevel, ServerLevel targetLevel, float yaw,
                              Function<Boolean, Entity> repositionEntity) {
        Entity teleportedEntity = repositionEntity.apply(false);
        if (teleportedEntity != null) {
            teleportedEntity.moveTo(x, y, z, yRot, xRot);
            teleportedEntity.setDeltaMovement(deltaMovement);
            teleportedEntity.setYHeadRot(yRot);
        }
        return teleportedEntity;
    }
}
