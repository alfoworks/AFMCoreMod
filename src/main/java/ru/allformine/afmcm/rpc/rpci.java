package ru.allformine.afmcm.rpc;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import ru.allformine.afmcm.References;

public class rpci
{
    public static void updateState(String state, long time) {
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence
                .Builder(References.nickname)
                .setBigImage(References.bigImageKey, References.serverName)
                .setSmallImage("icon", "AFMCoreMod")
                .setStartTimestamps(time)
                .setDetails(state)
                .build());
    }

    public static void initDiscord(long time) {
        DiscordRPC.discordRunCallbacks();
        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder()
                .setReadyEventHandler(user -> DiscordRPC.discordUpdatePresence(new DiscordRichPresence
                        .Builder(References.nickname)
                        .setBigImage(References.bigImageKey, References.serverName)
                        .setSmallImage("icon", "AFMCoreMod")
                        .setStartTimestamps(time)
                        .setDetails("хз")
                        .build()))
                .build();
        DiscordRPC.discordInitialize(References.rpcAppId, handlers, true);
    }
}
