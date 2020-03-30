package ru.allformine.afmcm.messaging;

import net.minecraft.client.Minecraft;

public class MessageDispatcher {
    public static void displayMessage(MessageType type, String message) {
        if (type == MessageType.NOTIFY_MESSAGE) {
            NotifyMessageRenderer.setMessage(message.length() > 40 ? message.substring(0, 40) : message);
        } else {
            Minecraft.getMinecraft().displayGuiScreen(new WindowedMessageGuiScreen(message.length() > 400 ? message.substring(0, 400) : message));
        }
    }
}
