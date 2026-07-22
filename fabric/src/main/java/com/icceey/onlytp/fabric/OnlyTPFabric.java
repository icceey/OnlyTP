package com.icceey.onlytp.fabric;

import com.icceey.onlytp.command.TeleportCommand;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;

public final class OnlyTPFabric implements ModInitializer {
    public static final String MODID = "onlytp";
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> TeleportCommand.register(dispatcher)
        );
        LOGGER.info("OnlyTP mod loaded!");
    }
}
