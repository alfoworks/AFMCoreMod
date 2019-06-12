package ru.allformine.afmcm.proxy;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.References;

public class NotifierProxy {

    private FMLNetworkEvent.ClientCustomPacketEvent event;

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        ByteBuf buf = event.getPacket().payload();
        ByteBufUtils.readUTF8String(buf);
        String text = ByteBufUtils.readUTF8String(buf);

        if(text.length() > 0) {
            ResourceLocation resourceLocation = new ResourceLocation("afmcm:nstart");
            SoundEvent soundEvent = new SoundEvent(resourceLocation);
            mc.player.playSound(soundEvent, mc.gameSettings.getSoundLevel(SoundCategory.MASTER), 1.0F);
            References.notifyText = Util.splitString(text);
            References.notifyDrawing = true;
        }
    }
}
