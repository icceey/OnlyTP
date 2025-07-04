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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

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
            source.sendFailure(Component.literal("只有玩家可以使用此命令"));
            return 0;
        }

        // 获取目标玩家
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");

        // 检查是否尝试传送到自己
        if (executor.equals(targetPlayer)) {
            source.sendFailure(Component.literal("禁止原地TP"));
            return 0;
        }

        // 检查目标玩家是否在线
        if (targetPlayer.isRemoved() || targetPlayer.hasDisconnected()) {
            source.sendFailure(Component.literal("目标玩家不在线"));
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
                0.05F,                    // 音量
                1.0F                     // 音调
        );

        // 执行传送
        executor.teleportTo(
                targetPlayer.serverLevel(),
                targetPlayer.getX(),
                targetPlayer.getY(),
                targetPlayer.getZ(),
                targetPlayer.getYRot(),
                targetPlayer.getXRot()
        );

        // 在目的地播放下界传送门音效
        targetPlayer.level().playSound(
                null,                    // 发送给所有玩家，无需特定玩家
                targetPlayer.getX(),      // 音效 X 坐标
                targetPlayer.getY(),      // 音效 Y 坐标
                targetPlayer.getZ(),      // 音效 Z 坐标
                SoundEvents.PORTAL_TRAVEL,// 下界传送门音效
                SoundSource.PLAYERS,     // 音效来源类别（玩家）
                0.05F,                    // 音量
                1.0F                     // 音调
        );

        source.sendSuccess(
                () -> Component.literal("已传送到 " + targetPlayer.getGameProfile().getName()),
                true
        );

        // 通知目标玩家有人传送到了他那里
        targetPlayer.sendSystemMessage(
                Component.literal(executor.getGameProfile().getName() + " 传送到了你这里")
        );

        return 1;
    }
}
