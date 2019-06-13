package ru.allformine.afmcm;

import net.arikia.dev.drpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
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
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.Logger;
import ru.allformine.afmcm.discord.rpci;
import ru.allformine.afmcm.gui.DreamHudGui;
import ru.allformine.afmcm.keyboard.DreamHudSwitch;
import ru.allformine.afmcm.keyboard.KeyBind;
import ru.allformine.afmcm.keyboard.KeyBinder;

import java.io.File;
import java.util.Collection;

@Mod(modid = "afmcm")
public class AFMCoreMod {
    public static Logger logger;
    public static Configuration config;

    private static int rpcTick = 0;
    private static long rpcTime = 0;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        logger = event.getModLog();

        config = new Configuration(new File("config", "AFMCoreMod.cfg"));
        config.load();

        References.rpcAppId = config.getString("rpcAppId", "discord", References.rpcAppId, "Secret stuff");
        References.serverName = config.getString("serverName", "discord", References.serverName, "Secret stuff");
        References.bigImageKey = config.getString("bigImageKey", "discord", References.bigImageKey, "Secret stuff");
        References.activateDreamHud = config.getBoolean("activateDreamHud", "dreamHud",
                References.activateDreamHud, "Activates dream hud");

        config.save();
    }
    @EventHandler
    public void init(FMLInitializationEvent event){
        KeyBinder.register(new DreamHudSwitch());
        //MinecraftForge.EVENT_BUS.register(self.class);
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
        if (mc.world == null || !mc.world.isRemote) {
            return;
        }

        if (rpcTick > 100) {
            rpcTick = 0;
        }

        rpcTick++;

        if (rpcTick == 100 && mc.getConnection() != null) {
            Collection<NetworkPlayerInfo> players = mc.getConnection().getPlayerInfoMap();

            DiscordRPC.discordUpdatePresence(rpci.getNewState(rpci.playerState.STATE_ON_SERVER, "(" + String.valueOf(players.size()) + " из " + String.valueOf(mc.getConnection().currentServerMaxPlayers) + ")", rpcTime));
        }
    }

    private boolean readyToRender(){
        if (Minecraft.getMinecraft().player.isCreative() || !References.activateDreamHud) {
            return false;
        }
        return true;
    }

    // =========== DreamHud
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if(readyToRender()) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH
                    || event.getType() == RenderGameOverlayEvent.ElementType.FOOD
                    || event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
                if (event.isCancelable()) {
                    event.setCanceled(true);
                }
            } else if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR || event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
                GuiIngameForge.left_height = 45;
                GuiIngameForge.right_height = 45;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if(readyToRender())
            new DreamHudGui(event).drawScreen();
    }
}
