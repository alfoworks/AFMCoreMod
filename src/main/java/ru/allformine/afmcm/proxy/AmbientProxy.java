package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import ru.allformine.afmcm.References;
import ru.allformine.afmcm.audioplayer.AudioPlayer;
import ru.allformine.afmcm.audioplayer.MarkErrorInputStream;

import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

public class AmbientProxy {
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        ByteBuf buf = event.packet.payload();
        byte mode = buf.readByte();

        switch (mode) {
            case 1: // начать проигрывание
                if (References.activePlayer != null) {
                    References.activePlayer.close();
                    References.activePlayer = null;
                }

                ByteBufUtils.readUTF8String(buf);
                String url = ByteBufUtils.readUTF8String(buf);

                try {
                    InputStream is = new URL(url).openStream();
                    BufferedInputStream bis = new BufferedInputStream(is);

                    References.activePlayer = new AudioPlayer(AudioSystem.getAudioInputStream(new MarkErrorInputStream(bis)));
                    References.activePlayer.start();
                } catch (Exception e) {
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Error playing music from URL " + url));

                    e.printStackTrace();
                }
                break;
            case 2: // остановить проигрывание
                if (References.activePlayer != null) {
                    References.activePlayer.close();
                    References.activePlayer = null;
                }
                break;
        }
    }
}
