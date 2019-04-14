package ru.allformine.afmcm;

import net.arikia.dev.drpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.Logger;
import ru.allformine.afmcm.discord.rpci;

import java.io.File;
import java.util.Collection;

@Mod(modid = "afmcm")
public class AFMCoreMod {
    public static Logger logger;

    private static int rpcTick = 0;
    private static long rpcTime = 0;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        logger = event.getModLog();

        Configuration config = new Configuration(new File("config", "AFMCoreMod.cfg"));
        config.load();

        References.rpcAppId = config.getString("rpcAppId", "discord", References.rpcAppId, "Secret stuff");
        References.serverName = config.getString("serverName", "discord", References.serverName, "Secret stuff");
        References.bigImageKey = config.getString("bigImageKey", "discord", References.bigImageKey, "Secret stuff");

        config.save();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        References.nickname = Minecraft.getMinecraft().player.getDisplayNameString();

        rpci.initDiscord();
        rpci.getNewState(rpci.playerState.STATE_IN_MENU, "", System.currentTimeMillis() / 1000L);
    }

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
}
