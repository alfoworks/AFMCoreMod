package ru.allformine.afmcm.keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.References;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;

public class CopyItemIdKey implements KeyBind {
    private static KeyBinding keyBinding = new KeyBinding("Copy held item ID", Keyboard.KEY_COMMA, References.category);
    @Override
    public void onPress(InputEvent.KeyInputEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack itemStack = player.getActiveItemStack();
        if(!itemStack.isEmpty()){
            String name = Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString();
            StringSelection selection = new StringSelection(name);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
