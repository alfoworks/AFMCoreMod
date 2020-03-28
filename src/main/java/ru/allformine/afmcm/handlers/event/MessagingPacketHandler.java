package ru.allformine.afmcm.handlers.event;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.messaging.MessageDispatcher;
import ru.allformine.afmcm.messaging.MessageType;

public class MessagingPacketHandler {
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        ByteBuf buf = event.getPacket().payload();

        String messageText = ByteBufUtils.readUTF8String(buf);

        MessageType messageType = buf.readBoolean() ? MessageType.WINDOWED_MESSAGE : MessageType.NOTIFY_MESSAGE;

        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(messageText));

        MessageDispatcher.displayMessage(messageType, messageText);
    }
}
