package ru.allformine.afmcm.messaging;

import net.minecraft.client.Minecraft;

public class MessageDispatcher {
    static NotifyMessageRenderer notifyRenderer = new NotifyMessageRenderer();

    public static void displayMessage(MessageType type, String message) {
        if (type == MessageType.NOTIFY_MESSAGE) {
            NotifyMessageRenderer.setMessage(message);
        } else {
            Minecraft.getMinecraft().displayGuiScreen(new WindowedMessageGuiScreen());
        }
    }
}
