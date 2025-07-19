package com.icceey.onlytp;

import com.icceey.onlytp.command.TeleportCommand;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(OnlyTP.MODID)
public class OnlyTP {
    public static final String MODID = "onlytp";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OnlyTP() {
        NeoForge.EVENT_BUS.register(this);
        LOGGER.info("OnlyTP mod loaded for NeoForge!");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        TeleportCommand.register(event.getDispatcher());
    }
}