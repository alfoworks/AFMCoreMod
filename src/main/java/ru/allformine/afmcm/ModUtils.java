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

    private static int clamp(final int min, final int max, final int value) {
        return Math.min(max, Math.max(value, min));
    }

    public static int colorARGBtoInt(final int alpha, final int red, final int green, final int blue) {
        return (clamp(0, 255, alpha) << 24)
                + (clamp(0, 255, red) << 16)
                + (clamp(0, 255, green) << 8)
                + clamp(0, 255, blue);
    }
}
