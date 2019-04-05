package ru.allformine.afmcm.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
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
                ByteBufUtils.readUTF8String(buf);

                new MusicThread(ByteBufUtils.readUTF8String(buf)).start();

                if(References.musicEnabled) {
                    References.activePlayer.turnOn();
                } else {
                    References.activePlayer.turnOff();
                }

                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentTranslation("messages.ambient.notify", Keyboard.getKeyName(AFMCoreMod.SWITCH_MUSIC.getKeyCode())));
                break;
            case 2: // остановить проигрывание
                if(References.activePlayer != null) {
                    References.activePlayer.close();
                    References.activePlayer = null;
                }
                break;
        }
    }
}
