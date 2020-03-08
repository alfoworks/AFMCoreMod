package ru.allformine.afmcm;

import io.netty.channel.ChannelPipeline;
import net.arikia.dev.drpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.init.SoundEvents;
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
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;
import ru.allformine.afmcm.discord.RPC;
import ru.allformine.afmcm.gui.FactionsGui;
import ru.allformine.afmcm.handlers.event.DreamHudConfigEventHandler;
import ru.allformine.afmcm.handlers.event.DreamHudRenderEventHandler;
import ru.allformine.afmcm.handlers.event.MPScreenEventHandler;
import ru.allformine.afmcm.handlers.packet.PacketHandler;
import ru.allformine.afmcm.keyboard.CopyItemIdKey;
import ru.allformine.afmcm.keyboard.KeyBind;
import ru.allformine.afmcm.keyboard.KeyBinder;
import ru.allformine.afmcm.proxy.FactionsProxy;

import java.io.File;
import java.util.Collection;

@Mod(modid = "afmcm")
public class AFMCoreMod {
    public static Logger logger;
    public static Configuration config;

    private static int rpcTick = 0;
    private static long rpcTime = 0;

    private long ticksFromStart = 0;
    private boolean gameLoadedSoundFlag = false;

    private boolean added = false;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new DreamHudConfigEventHandler());
        MinecraftForge.EVENT_BUS.register(new DreamHudRenderEventHandler());
        MinecraftForge.EVENT_BUS.register(new MPScreenEventHandler());

        logger = event.getModLog();

        config = new Configuration(new File("config", "AFMCoreMod.cfg"));
        config.load();

        ModConfig.rpcAppId = config.getString("rpcAppId", "discord", ModConfig.rpcAppId, "Secret stuff");
        ModConfig.serverName = config.getString("serverName", "discord", ModConfig.serverName, "Secret stuff");
        ModConfig.bigImageKey = config.getString("bigImageKey", "discord", ModConfig.bigImageKey, "Secret stuff");
        ModConfig.dreamHudEnabled = config.getBoolean("dreamHudEnabled", "dreamhud", ModConfig.dreamHudEnabled, "Noire");

        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        KeyBinder.register(new CopyItemIdKey());

        FactionsProxy factionsHandler = new FactionsProxy();
        NetworkRegistry.INSTANCE.newEventDrivenChannel("factions").register(factionsHandler);
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
        ModStatics.playerNickname = Minecraft.getMinecraft().getSession().getUsername();

        RPC.initDiscord();
        DiscordRPC.discordUpdatePresence(RPC.getNewState(RPC.playerState.STATE_IN_MENU, "", System.currentTimeMillis() / 1000L));
    }

    // =========== DISCORD
    @SubscribeEvent
    public void onLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        rpcTime = System.currentTimeMillis() / 1000L;

        System.out.println("Changing Discord RPC status (ClientConnectedToServerEvent)");

        DiscordRPC.discordUpdatePresence(RPC.getNewState(RPC.playerState.STATE_ON_SERVER, "", rpcTime));
    }

    @SubscribeEvent
    public void onLoggedOut(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        rpcTime = System.currentTimeMillis() / 1000L;

        System.out.println("Changing Discord RPC status (ClientDisconnectedFromServerEvent)");

        DiscordRPC.discordUpdatePresence(RPC.getNewState(RPC.playerState.STATE_IN_MENU, "", rpcTime));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) { // обновление Discord RPC каждые 5 сек, если игрок на сервере
        Minecraft mc = Minecraft.getMinecraft();                // (для показа онлайна)

        if (!gameLoadedSoundFlag) ticksFromStart++;

        if (!gameLoadedSoundFlag && ticksFromStart > 60) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F));

            gameLoadedSoundFlag = true;
        }

        if (mc.getConnection() != null) {
            ChannelPipeline pipe = mc.getConnection().getNetworkManager().channel().pipeline();

            if (!added) {
                pipe.addBefore("packet_handler", "PacketHandler", new PacketHandler());

                added = true;
            }
        } else if (added) {
            added = false;
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

            DiscordRPC.discordUpdatePresence(RPC.getNewState(RPC.playerState.STATE_ON_SERVER, " (" + players.size() + " из " + mc.getConnection().currentServerMaxPlayers + ")", rpcTime));
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (ModStatics.factionText.length() > 0)
            new FactionsGui(ModStatics.factionText, Minecraft.getMinecraft(), event);
    }
}
