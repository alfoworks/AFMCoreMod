package ru.allformine.afmcm;

import ru.allformine.afmcm.audioplayer.AudioPlayer;
import java.util.ArrayList;
import java.util.List;

public class References {
    public static List<String> notifyText = new ArrayList<>();
    static int notifyTicks = 0;
    public static boolean notifyDrawing = false;

    public static String territoryText = "";

    public static String rpcAppId = "";
    public static String bigImageKey = "";
    public static String serverName = "";
    public static String nickname = "";

    public static AudioPlayer activePlayer;
    public static boolean musicEnabled = true;
    public static String activeMusicUrl;
}
