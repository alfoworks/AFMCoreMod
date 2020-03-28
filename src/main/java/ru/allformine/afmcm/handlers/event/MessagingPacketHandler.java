package ru.allformine.afmcm.handlers.event;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.allformine.afmcm.messaging.MessageDispatcher;
import ru.allformine.afmcm.messaging.MessageType;

public class MessagingPacketHandler {
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        PacketBuffer buffer = new PacketBuffer(event.getPacket().payload());

        String messageText = new String(buffer.readByteArray());

        MessageType messageType;

        if (buffer.readInt() == 0) {
            messageType = MessageType.NOTIFY_MESSAGE;
        } else {
            messageType = MessageType.WINDOWED_MESSAGE;
        }

        System.out.println(messageText);

        MessageDispatcher.displayMessage(messageType, messageText);
    }
}
