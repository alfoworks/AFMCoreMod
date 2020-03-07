package ru.allformine.afmcm.keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.ModStatics;
import ru.allformine.afmcm.ModUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyItemIdKey implements KeyBind {
    private static KeyBinding keyBinding = new KeyBinding(I18n.format("afmcm.key.copyitemid"), Keyboard.KEY_COMMA, ModStatics.category);

    @Override
    public void onPress(InputEvent.KeyInputEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);

        if (itemStack.isEmpty()) {
            return;
        }

        ResourceLocation registryName = itemStack.getItem().getRegistryName();

        if(registryName == null) {

            ModUtils.sendErrorMessage(player, I18n.format("afmcm.text.errorcopy"));
            return;
        }

        String name = registryName.toString();
        int metadata = itemStack.getMetadata();

        if(metadata != 0){
            name =  name + ":" + metadata;
        }

        StringSelection selection = new StringSelection(name);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        ModUtils.sendStyledMessage(player, I18n.format("afmcm.text.okcopy", name));
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
