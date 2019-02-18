package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import ru.allformine.afmcm.References;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AmbientProxy {
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        ByteBuf buf = event.packet.payload();

        switch (buf.readByte()) {
            case 1: // начать проигрывание
                String url = ByteBufUtils.readUTF8String(buf);

                try {
                    AudioInputStream as1 = AudioSystem.getAudioInputStream(new URL(url));
                    AudioFormat af = as1.getFormat();
                    Clip clip = AudioSystem.getClip();
                    DataLine.Info info = new DataLine.Info(Clip.class, af);

                    Line line1 = AudioSystem.getLine(info);

                    if (!line1.isOpen()) {
                        clip.open(as1);
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                        clip.start();

                        References.activeBackgroundMusic = clip;
                    }
                } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Error playing music from URL " + url));
                }
                break;
            case 2: // остановить проигрывание
                if (References.activeBackgroundMusic != null) {
                    References.activeBackgroundMusic.stop();
                    References.activeBackgroundMusic = null;
                }
                break;
        }
    }
}
