package com.icceey.onlytp.command;

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
                    .map(profile -> profile.getName()),
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
            source.sendFailure(Component.literal("你不能传送到自己"));
            return 0;
        }

        // 检查目标玩家是否在线
        if (!targetPlayer.isAlive() || targetPlayer.hasDisconnected()) {
            source.sendFailure(Component.literal("目标玩家不在线"));
            return 0;
        }

        // 执行传送
        executor.teleportTo(
            targetPlayer.serverLevel(),
            targetPlayer.getX(),
            targetPlayer.getY(),
            targetPlayer.getZ(),
            targetPlayer.getYRot(),
            targetPlayer.getXRot()
        );

        source.sendSuccess(
            () -> Component.literal("已传送到 " + targetPlayer.getGameProfile().getName()),
            true
        );

        return 1;
    }
}
