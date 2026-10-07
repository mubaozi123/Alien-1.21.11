/*
 * Decompiled with CFR 0.152.
 */
package shit.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlBackend;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.client.util.Window;
import org.lwjgl.opengl.GL11C;
import shit.util.Pos;

@Environment(value=EnvType.CLIENT)
public class Blur2 {
    private int count111 = -1;
    private GpuTexture gpuTexture2;
    public int count170 = -1;
    public double value167;
    public int count174;
    public int count162;

    public Blur2(double d) {
        this.value167 = d;
        this.init2();
    }

    private void init2() {
        GlTexture glTexture;
        block4: {
            block3: {
                GpuTexture gpuTexture;
                String string;
                block2: {
                    GpuTexture gpuTexture2;
                    Window window = MinecraftClient.getInstance().getWindow();
                    this.count174 = Math.max(1, (int)((double)window.getFramebufferWidth() * this.value167));
                    string = Pos.getText67();
                    this.count162 = Math.max(1, (int)((double)window.getFramebufferHeight() * this.value167));
                    int n = 15;
                    gpuTexture = gpuTexture2 = (this.gpuTexture2 = RenderSystem.getDevice().createTexture(() -> "alien blur framebuffer", n, TextureFormat.RGBA8, this.count174, this.count162, 1, 1));
                    if (string != null) break block2;
                    if (!(gpuTexture instanceof GlTexture)) break block3;
                    gpuTexture = gpuTexture2;
                }
                glTexture = (GlTexture)gpuTexture;
                if (string == null) break block4;
            }
            throw new IllegalStateException("Blur framebuffer requires OpenGL texture backend");
        }
        this.count170 = glTexture.getGlId();
        this.count111 = glTexture.getOrCreateFramebuffer(((GlBackend)RenderSystem.getDevice()).getBufferManager(), null);
        GlStateManager._glBindFramebuffer((int)36160, (int)this.count111);
        GlStateManager._bindTexture((int)this.count170);
        GlStateManager._texParameter((int)3553, (int)10242, (int)33071);
        GlStateManager._texParameter((int)3553, (int)10243, (int)33071);
        GlStateManager._texParameter((int)3553, (int)10241, (int)9729);
        GlStateManager._texParameter((int)3553, (int)10240, (int)9729);
        GL11C.glDrawBuffer((int)36064);
        GL11C.glReadBuffer((int)36064);
        this.m570();
    }

    public void m554() {
        GlStateManager._glBindFramebuffer((int)36160, (int)this.count111);
        GL11C.glDrawBuffer((int)36064);
    }

    public int getInt34() {
        return this.count111;
    }

    public void m78() {
        GlStateManager._viewport((int)0, (int)0, (int)this.count174, (int)this.count162);
    }

    public void m570() {
        GlStateManager._glBindFramebuffer((int)36160, (int)GlStateManager.getFrameBuffer((int)36160));
    }

    public void m216() {
        String string = Pos.getText67();
        Blur2 blur2 = this;
        if (string == null) {
            if (blur2.gpuTexture2 != null) {
                this.gpuTexture2.close();
            }
            this.gpuTexture2 = null;
            this.count111 = -1;
            this.count170 = -1;
            blur2 = this;
        }
        blur2.init2();
    }

    public void m485() {
        String string = Pos.getText67();
        Blur2 blur2 = this;
        if (string == null) {
            if (blur2.gpuTexture2 != null) {
                this.gpuTexture2.close();
            }
            this.gpuTexture2 = null;
            this.count111 = -1;
            this.count170 = -1;
            this.count174 = 0;
            blur2 = this;
        }
        blur2.count162 = 0;
    }
}

