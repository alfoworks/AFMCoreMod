package ru.allformine.afmcm;

import net.arikia.dev.drpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;
import ru.allformine.afmcm.discord.rpci;
import ru.allformine.afmcm.gui.FactionsGui;
import ru.allformine.afmcm.keyboard.CopyItemIdKey;
import ru.allformine.afmcm.keyboard.KeyBind;
import ru.allformine.afmcm.keyboard.KeyBinder;
import ru.allformine.afmcm.listener.DiscordListener;
import ru.allformine.afmcm.proxy.FactionsProxy;
import ru.allformine.afmcm.proxy.ScreenshotProxy;

import java.io.File;

@Mod(modid = "afmcm")
public class AFMCoreMod {
    public static Logger logger;
    public static Configuration config;
    @SuppressWarnings("WeakerAccess")
    public static FMLEventChannel channel;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        logger = event.getModLog();

        config = new Configuration(new File("config", "AFMCoreMod.cfg"));
        config.load();

        References.rpcAppId = config.getString("rpcAppId", "discord", References.rpcAppId, "Secret stuff");
        References.serverName = config.getString("serverName", "discord", References.serverName, "Secret stuff");
        References.bigImageKey = config.getString("bigImageKey", "discord", References.bigImageKey, "Secret stuff");

        config.save();
    }
    @EventHandler
    public void init(FMLInitializationEvent event){
        KeyBinder.register(new CopyItemIdKey());

        ScreenshotProxy screenshotHandler = new ScreenshotProxy();
        (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("AN3234234A")).register(screenshotHandler);
        MinecraftForge.EVENT_BUS.register(screenshotHandler);

        FactionsProxy factionsHandler = new FactionsProxy();
        (channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("factions")).register(factionsHandler);
        MinecraftForge.EVENT_BUS.register(factionsHandler);

        MinecraftForge.EVENT_BUS.register(new DiscordListener());
    }

    @SubscribeEvent
    public void onKeyInputEvent(InputEvent.KeyInputEvent event){
        for (KeyBind keyBind: KeyBinder.keyBinds){
            if(keyBind.getBinding().isPressed()){
                keyBind.onPress(event);
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        References.nickname = Minecraft.getMinecraft().getSession().getUsername();

        rpci.initDiscord();
        DiscordRPC.discordUpdatePresence(rpci.getNewState(rpci.playerState.STATE_IN_MENU, "", System.currentTimeMillis() / 1000L));
    }

    // =========== DISCORD


    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (References.factionText.length() > 0)
            new FactionsGui(References.factionText, Minecraft.getMinecraft(), event);
    }
}
