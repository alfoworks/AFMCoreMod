package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class DreamHudGui {
    private RenderGameOverlayEvent event;

    public DreamHudGui(RenderGameOverlayEvent event) {
        this.event = event;
    }

    public void drawScreen() {
        Minecraft mc = Minecraft.getMinecraft(); // 53, 25

        // биндим файл с хуйней для худа
        mc.renderEngine.bindTexture(new ResourceLocation("afmcm", "textures/afmcm_textures.png"));

        // рисуем основу худа
        mc.ingameGUI.drawTexturedModalRect(8, 8, 0, 0, 188, 49);

        // рисуем серые прогрессбары
        mc.ingameGUI.drawTexturedModalRect(61, 33, 0, 69, 131, 10);
        mc.ingameGUI.drawTexturedModalRect(61, 43, 0, 69, 131, 10);
        mc.ingameGUI.drawTexturedModalRect(event.getResolution().getScaledWidth() / 2 - 91, event.getResolution().getScaledHeight() - 33, 0, 79, 182, 12);

        // рисуем прогрессбары
        //// хп
        float health_oneUnit = (float) 131 / mc.player.getMaxHealth();
        int health_currentWidth = (int) (health_oneUnit * mc.player.getHealth());

        mc.ingameGUI.drawTexturedModalRect(61, 33, 0, 49, health_currentWidth, 10);

        //// голод
        float hunger_oneUnit = (float) 131 / 20;
        int hunger_currentWidth = (int) (hunger_oneUnit * mc.player.getFoodStats().getFoodLevel());

        mc.ingameGUI.drawTexturedModalRect(61, 43, 0, 59, hunger_currentWidth, 10);

        //// опыт
        int exp_width = (int) (mc.player.experience * 183.0F);
        mc.ingameGUI.drawTexturedModalRect(event.getResolution().getScaledWidth() / 2 - 91, event.getResolution().getScaledHeight() - 33, 0, 91, exp_width, 12);

        // рисуем ник
        String nick = mc.getSession().getUsername();
        mc.fontRenderer.drawStringWithShadow(nick, 60 + (float) (131 - mc.fontRenderer.getStringWidth(nick)) / 2, 18, 0xFFFFFF);

        // wtf?
        EntityLivingBase ent = mc.player;
        int posX = 30, posY = 150, scale = 40;

        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(160.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (1 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float) Math.atan((double) (1 / 40.0F)) * 20.0F;
        ent.rotationYaw = (float) Math.atan((double) (1 / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float) Math.atan((double) (1 / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.popMatrix();

        //остальной текст (уменьшенный)
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.8F, 0.8F, 0.8F);
        ////опыт в цифрах на худе
        String exp = String.valueOf(Minecraft.getMinecraft().player.experienceLevel);
        mc.fontRenderer.drawStringWithShadow(exp, 11 + (float) (13 - mc.fontRenderer.getStringWidth(exp)) / 2, 47, 0x56ffa6);
        GlStateManager.popMatrix();
    }
}
