package ru.allformine.afmcm;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ModUtils {
    public static void sendStyledMessage(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(String.format("%sAFMCoreMod %s> %s", TextFormatting.YELLOW, TextFormatting.AQUA, message)));
    }

    public static void sendErrorMessage(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(String.format("%sAFMCoreMod > %s", TextFormatting.RED, message)));
    }
}
