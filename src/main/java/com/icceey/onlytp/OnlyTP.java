package com.icceey.onlytp;

import com.icceey.onlytp.command.TeleportCommand;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(OnlyTP.MODID)
public class OnlyTP {
    public static final String MODID = "onlytp";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OnlyTP() {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("OnlyTP mod loaded!");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        TeleportCommand.register(event.getDispatcher());
    }
}
