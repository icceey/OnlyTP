package com.icceey.onlytp.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import java.util.stream.IntStream;

public class TeleportCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tlp")
                .then(Commands.argument("player", EntityArgument.player())
                        .suggests(ONLINE_PLAYERS_SUGGESTION)
                        .executes(TeleportCommand::execute)
                )
        );
    }

    private static final SuggestionProvider<CommandSourceStack> ONLINE_PLAYERS_SUGGESTION =
            (context, builder) -> {
                // 获取所有在线玩家的名称，但排除命令执行者自己
                return SharedSuggestionProvider.suggest(
                        context.getSource().getServer().getPlayerList().getPlayers().stream()
                                .filter(player -> !player.equals(context.getSource().getEntity()))
                                .map(ServerPlayer::getGameProfile)
                                .map(GameProfile::getName),
                        builder
                );
            };

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        // 确保命令执行者是玩家
        if (!(source.getEntity() instanceof ServerPlayer executor)) {
            source.sendFailure(translatableWithFallback("commands.onlytp.only_player"));
            return 0;
        }

        // 获取目标玩家
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");

        // 检查是否尝试传送到自己
        if (executor.equals(targetPlayer) && !executor.hasPermissions(2)) {
            // 如果没有作弊权限，禁止原地TP
            source.sendFailure(translatableWithFallback("commands.onlytp.no_self_tp"));
            return 0;
        }

        // 检查目标玩家是否在线并且存活
        if (!targetPlayer.isAlive() || targetPlayer.hasDisconnected()) {
            source.sendFailure(translatableWithFallback("commands.onlytp.target_dead_offline"));
            return 0;
        }

        // 检查当前玩家是否在线并且存活
        if (!executor.isAlive() || executor.hasDisconnected()) {
            source.sendFailure(translatableWithFallback("commands.onlytp.executor_dead_offline"));
            return 0;
        }

        // 在原位置播放下界传送门音效
        executor.level().playSound(
                null,                    // 发送给所有玩家，无需特定玩家
                executor.getX(),         // 音效 X 坐标
                executor.getY(),         // 音效 Y 坐标
                executor.getZ(),         // 音效 Z 坐标
                SoundEvents.PORTAL_TRAVEL,// 下界传送门音效
                SoundSource.PLAYERS,     // 音效来源类别（玩家）
                0.1F,                    // 音量
                1.0F                     // 音调
        );

        // 在出发点生成末影人粒子效果
        spawnTeleportParticles(executor, executor.serverLevel(), ParticleTypes.PORTAL);

        // 执行传送
        executor.teleportTo(
                targetPlayer.serverLevel(),
                targetPlayer.getX(),
                targetPlayer.getY(),
                targetPlayer.getZ(),
                targetPlayer.getYRot(),
                targetPlayer.getXRot()
        );

        // 发送成功消息
        source.sendSuccess(() -> translatableWithFallback("commands.onlytp.success", targetPlayer.getGameProfile().getName()), true);

        // 通知目标玩家有人传送到了他那里
        targetPlayer.sendSystemMessage(translatableWithFallback("commands.onlytp.notify_target", executor.getGameProfile().getName()));

        // 在目的地播放下界传送门音效
        targetPlayer.level().playSound(
                null,                    // 发送给所有玩家，无需特定玩家
                targetPlayer.getX(),      // 音效 X 坐标
                targetPlayer.getY(),      // 音效 Y 坐标
                targetPlayer.getZ(),      // 音效 Z 坐标
                SoundEvents.PORTAL_TRAVEL,// 下界传送门音效
                SoundSource.PLAYERS,     // 音效来源类别（玩家）
                0.1F,                    // 音量
                1.0F                     // 音调
        );

        // 在目的地生成末影人粒子效果
        spawnTeleportParticles(targetPlayer, targetPlayer.serverLevel(), ParticleTypes.REVERSE_PORTAL);

        return 1;
    }

    /**
     * 在玩家周围生成传送粒子效果
     *
     * @param player 作为粒子生成中心的玩家
     * @param level 要在其中生成粒子的世界
     * @param particleType 要生成的粒子类型
     */
    private static void spawnTeleportParticles(ServerPlayer player, ServerLevel level, ParticleOptions particleType) {
        RandomSource random = level.getRandom();
        IntStream.range(0, 50).forEach(i -> {
            double x = player.getX() + (random.nextDouble() - 0.5) * 2.0;
            double y = player.getY() + random.nextDouble() * 2.0;
            double z = player.getZ() + (random.nextDouble() - 0.5) * 2.0;
            level.sendParticles(
                    particleType,
                    x, y, z,
                    1, // 粒子数量
                    0, 0, 0, // 速度
                    0.1 // 速度因子
            );
        });
    }

    private static Component translatableWithFallback(String key, Object... args) {
        String serverText = Component.translatable(key).getString();
        return Component.translatableWithFallback(key, serverText, args);
    }
}
