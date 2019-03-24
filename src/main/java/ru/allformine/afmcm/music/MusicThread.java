package ru.allformine.afmcm.music;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import ru.allformine.afmcm.References;
import ru.allformine.afmcm.audioplayer.AudioPlayer;
import ru.allformine.afmcm.audioplayer.MarkErrorInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

public class MusicThread extends Thread {
    private String url;

    public MusicThread(String url) {
        this.url = url;
    }

    public void run() {
        try {
            InputStream is = new URL(url).openStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            References.activePlayer = new AudioPlayer(AudioSystem.getAudioInputStream(new MarkErrorInputStream(bis)));
            References.activePlayer.start();
        } catch (Exception e) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Error playing music from URL " + url));

            e.printStackTrace();
        }
    }
}
