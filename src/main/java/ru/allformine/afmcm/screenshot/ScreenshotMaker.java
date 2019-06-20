package ru.allformine.afmcm.screenshot;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

@SideOnly(Side.CLIENT)
public class ScreenshotMaker {
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static byte[] getScreenshotByteArray(int displayWidth, int displayHeight, Framebuffer framebuffer, int imgType) {
        try {
            if (OpenGlHelper.isFramebufferEnabled()) {
                displayWidth = framebuffer.framebufferTextureWidth;
                displayHeight = framebuffer.framebufferTextureHeight;
            }

            int k = displayWidth * displayHeight;

            if (pixelBuffer == null || pixelBuffer.capacity() < k) {
                pixelBuffer = BufferUtils.createIntBuffer(k);
                pixelValues = new int[k];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            pixelBuffer.clear();

            if (OpenGlHelper.isFramebufferEnabled()) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
                GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            } else {
                GL11.glReadPixels(0, 0, displayWidth, displayHeight, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
            }

            pixelBuffer.get(pixelValues);
            TextureUtil.processPixelValues(pixelValues, displayWidth, displayHeight);
            BufferedImage bufferedimage;

            if (OpenGlHelper.isFramebufferEnabled()) {
                bufferedimage = new BufferedImage(framebuffer.framebufferWidth, framebuffer.framebufferHeight, imgType);
                int l = framebuffer.framebufferTextureHeight - framebuffer.framebufferHeight;

                for (int i1 = l; i1 < framebuffer.framebufferTextureHeight; ++i1) {
                    for (int j1 = 0; j1 < framebuffer.framebufferWidth; ++j1) {
                        bufferedimage.setRGB(j1, i1 - l, pixelValues[i1 * framebuffer.framebufferTextureWidth + j1]);
                    }
                }
            } else {
                bufferedimage = new BufferedImage(displayWidth, displayHeight, imgType);
                bufferedimage.setRGB(0, 0, displayWidth, displayHeight, pixelValues, 0, displayWidth);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                ImageIO.write(bufferedimage, "png", out);
                return out.toByteArray();
            } catch (IOException e) {
                return null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}