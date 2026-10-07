/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.module.render.Ambience;
import shit.module.render.Fullbright;
import shit.module.render.NoRender;
import shit.render.Lightmap;

@Environment(value=EnvType.CLIENT)
@Mixin(value={LightmapTextureManager.class})
public class LightTextureMixin {
    @Final
    @Shadow
    private MappableRingBuffer buffer;
    @Final
    @Shadow
    private GpuTextureView glTextureView;

    @Inject(method={"getDarkness(Lnet/minecraft/entity/LivingEntity;FF)F"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noDarkness(LivingEntity livingEntity, float f, float f2, CallbackInfoReturnable callbackInfoReturnable) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.darkness.getObj()).booleanValue()) {
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(0.0f));
        }
    }

    @Inject(method={"update(F)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$worldColor(float f, CallbackInfo callbackInfo) {
        Fullbright fullbright = Fullbright.INSTANCE;
        if (fullbright != null && fullbright.isSet19()) {
            Lightmap.field33 = this.buffer;
            Lightmap.gpuTextureView3 = this.glTextureView;
            float g = ((Double)fullbright.gamma.getObj()).floatValue();
            Lightmap.writeLightmap(g, g, g);
            callbackInfo.cancel();
            return;
        }
        Ambience ambience = Ambience.INSTANCE;
        if (ambience == null || !ambience.isSet19() || !((Boolean)ambience.worldColorDraw.getObj()).booleanValue()) {
            return;
        }
        Lightmap.field33 = this.buffer;
        Lightmap.gpuTextureView3 = this.glTextureView;
        if (MinecraftClient.getInstance().world == null) {
            Lightmap.writeLightmap(1.0f, 1.0f, 1.0f);
            callbackInfo.cancel();
            return;
        }
        int n = (Integer)ambience.worldColor.getObj();
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Lightmap.writeLightmap(f2, f3, f4);
        callbackInfo.cancel();
    }
}

