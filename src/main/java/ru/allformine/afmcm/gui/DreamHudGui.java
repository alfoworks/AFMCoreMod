package ru.allformine.afmcm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class DreamHudGui {
    private RenderGameOverlayEvent event;

    public DreamHudGui(RenderGameOverlayEvent event) {
        this.event = event;
    }

    public void drawScreen() {
        Minecraft mc = Minecraft.getMinecraft(); // 53, 25
        int offset = 8;
        int healthY = 25;
        int hungerY = 35;

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();

        // биндим файл с хуйней для худа
        mc.renderEngine.bindTexture(new ResourceLocation("afmcm", "textures/afmcm_textures.png"));

        // рисуем основу худа
        mc.ingameGUI.drawTexturedModalRect(offset, offset, 0, 0, 188, 49);

        // рисуем серые прогрессбары
        mc.ingameGUI.drawTexturedModalRect(offset + 53, offset + healthY, 0, 69, 131, 10);
        mc.ingameGUI.drawTexturedModalRect(offset + 53, offset + hungerY, 0, 69, 131, 10);
        mc.ingameGUI.drawTexturedModalRect(event.getResolution().getScaledWidth() / 2 - 91, event.getResolution().getScaledHeight() - 33, 0, 79, 182, 12);

        // рисуем прогрессбары
        //// хп
        float health_oneUnit = (float) 131 / mc.player.getMaxHealth();
        int health_currentWidth = (int) (health_oneUnit * mc.player.getHealth());

        mc.ingameGUI.drawTexturedModalRect(offset + 53, offset + healthY, 0, 49, health_currentWidth, 10);

        //// голод
        float hunger_oneUnit = (float) 131 / 20;
        int hunger_currentWidth = (int) (hunger_oneUnit * mc.player.getFoodStats().getFoodLevel());

        mc.ingameGUI.drawTexturedModalRect(offset + 53, offset + hungerY, 0, 59, hunger_currentWidth, 10);

        //// опыт
        int exp_width = (int) (mc.player.experience * 183.0F);
        mc.ingameGUI.drawTexturedModalRect(event.getResolution().getScaledWidth() / 2 - 91, event.getResolution().getScaledHeight() - 33, 0, 91, exp_width, 12);

        // рисуем ник
        String nick = mc.getSession().getUsername();
        mc.fontRenderer.drawStringWithShadow(nick, offset + 52 + center(132, mc.fontRenderer.getStringWidth(nick)), offset + 10, 0xFFFFFF);

        // Код рендера бошки
        /*
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(50, 50, 100, 100);
        //drawPlayer(offset + 100, offset + 8 + 200, 170, -4, 0, mc.player);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        */

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        glScissor(offset + 9, 50, offset + 8, 44);

        drawPlayer(offset + 9 + 49, offset + 8 + 203, 122, -4, 0, mc.player);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glPopMatrix();

        //остальной уменьшенный текст

        ////Хп
        String healthString = String.format("%s/20", (int) mc.player.getHealth());
        mc.fontRenderer.drawStringWithShadow(healthString, offset + 52 + center(131, mc.fontRenderer.getStringWidth(healthString)), offset + healthY + 2, 0xFFFFFF);

        ////Еда
        String hungerString = String.format("%s/20", (int) mc.player.getFoodStats().getFoodLevel());
        mc.fontRenderer.drawStringWithShadow(hungerString, offset + 52 + center(131, mc.fontRenderer.getStringWidth(hungerString)), offset + hungerY + 2, 0xFFFFFF);

        //Опыт над хотбаром
        String expString = String.format("%s/180", (int) (mc.player.experience * 183.0F));
        mc.fontRenderer.drawStringWithShadow(expString, center(event.getResolution().getScaledWidth(), mc.fontRenderer.getStringWidth(expString)), event.getResolution().getScaledHeight() - 31, 0xFFFFFF);

        //Уровень опыта в худе
        String expLevelString = String.valueOf(mc.player.experienceLevel);
        mc.fontRenderer.drawStringWithShadow(expLevelString, offset + 2 + center(13, mc.fontRenderer.getStringWidth(expLevelString)), offset + 38, 0x56ffa6);
    }

    private float center(int width, int length) {
        return (float) (width - length) / 2;
    }

    private void drawPlayer(int x, int y, int size, final float rx, final float ry, final EntityLivingBase player) {
        int scaledFactor = 2;
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

        x /= scaledFactor;
        y /= scaledFactor;
        size /= scaledFactor;
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, 50.0f);
        GL11.glScalef((float) (-size), (float) size, (float) size);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        final float f2 = player.renderYawOffset;
        final float f3 = player.rotationYaw;
        final float f4 = player.rotationPitch;
        final float f5 = player.prevRotationYawHead;
        final float f6 = player.rotationYawHead;
        GL11.glRotatef(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-(float) Math.atan(ry / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        player.renderYawOffset = rx;
        player.rotationYaw = rx;
        player.rotationPitch = 0.0f;
        player.rotationYawHead = rx;
        player.prevRotationYawHead = rx;
        GL11.glTranslatef(0.0f, (float) player.getYOffset(), 0.0f);
        renderManager.playerViewY = 180.0f;
        renderManager.renderEntity((Entity) player, 0.0, 0.0, 0.0, 0.0f, 1.0f, true);
        player.renderYawOffset = f2;
        player.rotationYaw = f3;
        player.rotationPitch = f4;
        player.prevRotationYawHead = f5;
        player.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    private void glScissor(float x1, float x2, float y1, float y2) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        x1 *= sr.getScaleFactor();
        y1 *= sr.getScaleFactor();
        x2 *= sr.getScaleFactor();
        y2 *= sr.getScaleFactor();
        float temp;
        if (y1 > y2) {
            temp = y2;
            y2 = y1;
            y1 = temp;
        }
        GL11.glScissor((int) x1, (int) (Display.getHeight() - y2), (int) (x2 - x1), (int) (y2 - y1));
    }
}
