package ru.allformine.afmcm;

import net.arikia.dev.drpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.client.GuiIngameForge;
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
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;
import ru.allformine.afmcm.discord.rpci;
import ru.allformine.afmcm.gui.FactionsGui;
import ru.allformine.afmcm.keyboard.CopyItemIdKey;
import ru.allformine.afmcm.keyboard.KeyBind;
import ru.allformine.afmcm.keyboard.KeyBinder;
import ru.allformine.afmcm.proxy.FactionsProxy;
import ru.allformine.afmcm.proxy.ScreenshotProxy;

import java.io.File;
import java.util.Collection;

@Mod(modid = "afmcm")
public class AFMCoreMod {
    public static Logger logger;
    public static Configuration config;
    @SuppressWarnings("WeakerAccess")
    public static FMLEventChannel channel;

    private static int rpcTick = 0;
    private static long rpcTime = 0;

    private long ticksFromStart = 0;
    private boolean gameLoadedSoundFlag = false;

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
    public void onLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        rpcTime = System.currentTimeMillis() / 1000L;

        System.out.println("Changing Discord RPC status (ClientConnectedToServerEvent)");

        DiscordRPC.discordUpdatePresence(rpci.getNewState(rpci.playerState.STATE_ON_SERVER, "", rpcTime));
    }

    @SubscribeEvent
    public void onLoggedOut(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        rpcTime = System.currentTimeMillis() / 1000L;

        System.out.println("Changing Discord RPC status (ClientDisconnectedFromServerEvent)");

        DiscordRPC.discordUpdatePresence(rpci.getNewState(rpci.playerState.STATE_IN_MENU, "", rpcTime));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) { // обновление Discord RPC каждые 5 сек, если игрок на сервере
        Minecraft mc = Minecraft.getMinecraft();                // (для показа онлайна)

        if (!gameLoadedSoundFlag) ticksFromStart++;

        if (!gameLoadedSoundFlag && ticksFromStart > 60) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F));

            gameLoadedSoundFlag = true;
        }

        if (mc.world == null || !mc.world.isRemote) {
            return;
        }

//        if (rpcTick > 100) {
//            rpcTick = 0;
//        }

        rpcTick = (rpcTick + 1) % 100;

        if (rpcTick == 99 && mc.getConnection() != null) {
            Collection<NetworkPlayerInfo> players = mc.getConnection().getPlayerInfoMap();

            DiscordRPC.discordUpdatePresence(rpci.getNewState(rpci.playerState.STATE_ON_SERVER, "(" + players.size() + " из " + mc.getConnection().currentServerMaxPlayers + ")", rpcTime));
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (References.factionText.length() > 0)
            new FactionsGui(References.factionText, Minecraft.getMinecraft(), event);
    }
}
