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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
                    URLConnection con = new URL(url).openConnection();
                    InputStream bis = new MarkErrorInputStream(new BufferedInputStream(con.getInputStream()));

                    References.activePlayer = new AudioPlayer(bis);
                    References.activePlayer.play();
                } catch (Exception e) {
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Error playing music from URL " + url));
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
