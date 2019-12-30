package ru.allformine.afmcm.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import ru.allformine.afmcm.AFMCoreMod;
import ru.allformine.afmcm.ModConfig;
import ru.allformine.afmcm.ModStatics;

public class RPC {
    public enum playerState {
        STATE_IN_MENU,
        STATE_ON_SERVER
    }

    public static DiscordRichPresence getNewState(playerState state, String add, long time) {
        String text = "";

        if (state == playerState.STATE_IN_MENU) {
            text = " В меню";
        } else if (state == playerState.STATE_ON_SERVER) {
            text = " На сервере";
        }

        return new DiscordRichPresence.Builder(String.format("%s - %s", ModStatics.playerNickname,text)).setBigImage(ModConfig.bigImageKey, ModConfig.serverName).setSmallImage("icon", "AFMCoreMod").setStartTimestamps(time).setDetails(ModConfig.serverName + add).build();
    }

    public static void initDiscord() {
        DiscordRPC.discordRunCallbacks();
        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(user -> DiscordRPC.discordUpdatePresence(getNewState(playerState.STATE_IN_MENU, "", System.currentTimeMillis() / 1000L))).build();

        DiscordRPC.discordInitialize(ModConfig.rpcAppId, handlers, true);

        AFMCoreMod.logger.info("Initialized Discord RPC integration.");
    }
}
