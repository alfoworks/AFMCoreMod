package ru.allformine.afmcm.keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import ru.allformine.afmcm.References;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyItemIdKey implements KeyBind {
    private static KeyBinding keyBinding = new KeyBinding("Copy held item ID", Keyboard.KEY_COMMA, References.category);
    @Override
    public void onPress(InputEvent.KeyInputEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
        if(!itemStack.isEmpty()){
            ResourceLocation registryName = itemStack.getItem().getRegistryName();
            if(registryName == null) {
                player.sendMessage(new TextComponentString("Unable to copy registry name: It will cause NPE."));
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
            player.sendMessage(new TextComponentString("Copied \"" + name + "\""));
        }
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
