package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import ru.allformine.afmcm.AFMCoreMod;
import ru.allformine.afmcm.References;
import ru.allformine.afmcm.audioplayer.AudioPlayer;
import ru.allformine.afmcm.audioplayer.MarkErrorInputStream;
import ru.allformine.afmcm.music.MusicThread;

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

                References.activeMusicUrl = ByteBufUtils.readUTF8String(buf);

                new MusicThread(References.activeMusicUrl).run();

                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE+"[AmbientMusic] "+EnumChatFormatting.RESET+"Нажмите M для переключения музыки."));
                break;
            case 2: // остановить проигрывание
                if (References.activePlayer != null) {
                    References.activePlayer.close();
                    References.activePlayer = null;
                    References.activeMusicUrl = "";
                }
                break;
        }
    }
}
