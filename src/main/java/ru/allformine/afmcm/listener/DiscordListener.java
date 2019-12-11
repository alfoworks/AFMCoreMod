package ru.allformine.afmcm.listener;

import net.arikia.dev.drpc.DiscordRPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.discord.rpci;

import java.util.Collection;

public class DiscordListener {
    private static int rpcTick = 0;
    private static long rpcTime = 0;

    private long ticksFromStart = 0;
    private boolean gameLoadedSoundFlag = false;

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
}
