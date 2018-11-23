package ru.allformine.afmcm.rpc;

import ru.allformine.afmcm.*;
import net.arikia.dev.drpc.*;

public class rpci
{
    public static void start() {
        initDiscord();
        DiscordRPC.discordRunCallbacks();
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("\u0420\u045c\u0420\u0451\u0420\u0454: " + vars.Nick).setBigImage("icon", "AFMCoreMod").setStartTimestamps(vars.initTime).setDetails("\u0420\u040e\u0420µ\u0421\u0402\u0420\u0406\u0420µ\u0421\u0402 " + vars.ServerName).build());
    }

    private static void initDiscord() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(user -> DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("\u0420\u045c\u0420\u0451\u0420\u0454: " + vars.Nick).setBigImage("icon", "AFMCoreMod").setStartTimestamps(vars.initTime).setDetails("\u0420\u040e\u0420µ\u0421\u0402\u0420\u0406\u0420µ\u0421\u0402: " + vars.ServerName).build())).build();
        DiscordRPC.discordInitialize("515590062667464715", handlers, true);
    }
}
