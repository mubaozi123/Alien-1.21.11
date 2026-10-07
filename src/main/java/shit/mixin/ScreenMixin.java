/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.render.Ambience;
import shit.render.Lightmap;

@Environment(value=EnvType.CLIENT)
@Mixin(value={GuiRenderer.class})
public class ScreenMixin {
    @Inject(method={"render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"}, at={@At(value="HEAD")})
    private void alien$guiRenderHead(GpuBufferSlice gpuBufferSlice, CallbackInfo callbackInfo) {
        if (!ScreenMixin.isWorldColorActive()) {
            return;
        }
        Lightmap.writeLightmap(1.0f, 1.0f, 1.0f);
    }

    @Inject(method={"render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"}, at={@At(value="TAIL")})
    private void alien$guiRenderTail(GpuBufferSlice gpuBufferSlice, CallbackInfo callbackInfo) {
        if (!ScreenMixin.isSet157()) {
            return;
        }
        Ambience ambience = Ambience.INSTANCE;
        int n = (Integer)ambience.worldColor.getObj();
        float f = (float)(n >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n & 0xFF) / 255.0f;
        Lightmap.writeLightmap(f, f2, f3);
    }

    private static boolean isWorldColorActive() {
        Ambience ambience = Ambience.INSTANCE;
        if (ambience == null || !ambience.isSet19() || !((Boolean)ambience.worldColorDraw.getObj()).booleanValue()) {
            return false;
        }
        return Lightmap.field33 != null && Lightmap.gpuTextureView3 != null;
    }

    private static boolean isSet157() {
        if (!ScreenMixin.isWorldColorActive()) {
            return false;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        return minecraftClient.world != null && !(minecraftClient.currentScreen instanceof TitleScreen);
    }
}

