/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.AtmosphericFogModifier;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.render.Ambience;
import shit.module.render.AntiFog;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={AtmosphericFogModifier.class})
public class AtmosphericFogEnvironmentMixin {
    @Inject(method={"applyStartEndModifier(Lnet/minecraft/client/render/fog/FogData;Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/world/ClientWorld;FLnet/minecraft/client/render/RenderTickCounter;)V"}, at={@At(value="TAIL")})
    private void alien$noFog(FogData fogData, Camera camera, ClientWorld clientWorld, float f, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        if (AntiFog.isSet25() || NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.fog.getObj()).booleanValue()) {
            float f2;
            fogData.environmentalStart = f2 = 1.0E9f;
            fogData.environmentalEnd = f2;
            fogData.renderDistanceStart = f2;
            fogData.renderDistanceEnd = f2;
            fogData.skyEnd = f2;
            fogData.cloudEnd = f2;
            return;
        }
        Ambience ambience = Ambience.INSTANCE;
        if (ambience != null && ambience.isSet19() && ((Boolean)ambience.fogDistance.getObj()).booleanValue()) {
            fogData.environmentalStart = ambience.fogStart.getFloat35();
            fogData.environmentalEnd = ambience.fogEnd.getFloat35();
        }
    }
}

