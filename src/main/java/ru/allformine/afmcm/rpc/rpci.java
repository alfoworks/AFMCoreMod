package ru.allformine.afmcm.rpc;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import ru.allformine.afmcm.vars;

public class rpci
{
    public static void updateState(String state, long time) {
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence
                .Builder(vars.nickname)
                .setBigImage(vars.bigImageKey, vars.serverName)
                .setSmallImage("icon", "AFMCoreMod")
                .setStartTimestamps(time)
                .setDetails(state)
                .build());
    }

    public static void initDiscord(long time) {
        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder()
                .setReadyEventHandler(user -> DiscordRPC.discordUpdatePresence(new DiscordRichPresence
                        .Builder(vars.nickname)
                        .setBigImage(vars.bigImageKey, vars.serverName)
                        .setSmallImage("icon", "AFMCoreMod")
                        .setStartTimestamps(time)
                        .setDetails("В меню")
                        .build()))
                .build();
        DiscordRPC.discordInitialize(vars.rpcAppId, handlers, true);
    }
}
