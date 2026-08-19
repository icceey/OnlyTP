package com.icceey.onlytp.compat;

import com.icceey.onlytp.forge.OnlyTPForge;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.NetworkHooks;

import java.util.Locale;

public abstract class LegacyMinecraftCompatBase implements MinecraftCompat {
    @Override
    public boolean hasPermissionLevel(CommandSourceStack source, ServerPlayer player, int level) {
        return source.hasPermission(level);
    }

    @Override
    public Entity teleportAcrossDimensions(LivingEntity entity, ServerLevel targetLevel,
                                           double targetX, double targetY, double targetZ,
                                           float targetYRot, float targetXRot) {
        return entity.changeDimension(
                targetLevel,
                new DirectEntityTeleporter(
                        targetX,
                        targetY,
                        targetZ,
                        targetYRot,
                        targetXRot,
                        entity.getDeltaMovement()
                )
        );
    }

    @Override
    public void startRiding(ServerPlayer player, Entity vehicle) {
        player.startRiding(vehicle, true);
    }

    protected final boolean clientHasOnlyTP(ServerPlayer player) {
        Connection connection = player.connection.connection;
        if (connection.isMemoryConnection()) {
            return true;
        }
        ConnectionData connectionData = NetworkHooks.getConnectionData(connection);
        return connectionData != null && connectionData.getModList().contains(OnlyTPForge.MODID);
    }

    protected final String englishFallback(String key, Object... args) {
        String template = switch (key) {
            case "commands.onlytp.only_player" -> "Only players can use this command";
            case "commands.onlytp.no_self_tp" -> "Self-teleportation is prohibited";
            case "commands.onlytp.target_dead_offline" -> "Target player is dead or offline";
            case "commands.onlytp.executor_dead_offline" -> "You are dead or offline, cannot teleport";
            case "commands.onlytp.success" -> "Teleported to %s";
            case "commands.onlytp.notify_target" -> "%s teleported to you";
            default -> key;
        };
        return String.format(Locale.ROOT, template, args);
    }
}
