package ru.allformine.afmcm;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ModUtils {
    public static void sendStyledMessage(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(String.format("%sAFMCoreMod %s> %s", TextFormatting.YELLOW, TextFormatting.AQUA, message)));
    }

    public static void sendErrorMessage(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(String.format("%sAFMCoreMod > %s", TextFormatting.RED, message)));
    }

    public static GuiButton getButtonById(List<GuiButton> buttons, int id) {
        for (GuiButton button : buttons) {
            if (button.id == id) return button;
        }

        return buttons.get(0);
    }
}
