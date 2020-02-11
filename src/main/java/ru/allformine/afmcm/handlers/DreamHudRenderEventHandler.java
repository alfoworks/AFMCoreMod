package ru.allformine.afmcm.handlers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.allformine.afmcm.ModConfig;
import ru.allformine.afmcm.gui.DreamHudGui;

public class DreamHudRenderEventHandler {
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (!ModConfig.dreamHudEnabled || Minecraft.getMinecraft().player.isCreative() || Minecraft.getMinecraft().player.isSpectator())
            return;

        new DreamHudGui(event).drawScreen();
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (!ModConfig.dreamHudEnabled) return;

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.AIR) || event.getType().equals(RenderGameOverlayEvent.ElementType.ARMOR)) {
            GuiIngameForge.left_height = 45;
            GuiIngameForge.right_height = 45;
        }

        event.setCanceled(event.getType().equals(RenderGameOverlayEvent.ElementType.EXPERIENCE)
                || event.getType().equals(RenderGameOverlayEvent.ElementType.FOOD)
                || event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH));
    }
}
