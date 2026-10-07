/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.module.render.Camera;

@Environment(value=EnvType.CLIENT)
@Mixin(value={GameRenderer.class})
public class GameRendererCameraMixin {
    @Shadow
    private float viewDistanceBlocks;

    @Inject(method={"getFov(Lnet/minecraft/client/render/Camera;FZ)F"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$getFov(net.minecraft.client.render.Camera camera, float f, boolean bl, CallbackInfoReturnable callbackInfoReturnable) {
        Camera camera2 = Camera.INSTANCE;
        if (camera2 == null || !camera2.isSet70()) {
            return;
        }
        if (bl) {
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(camera2.fovValue.getFloat35()));
        } else {
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(camera2.itemFov.getFloat35()));
        }
    }

    @Inject(method={"getBasicProjectionMatrix(F)Lorg/joml/Matrix4f;"}, at={@At(value="RETURN")}, cancellable=true)
    private void m891(float f, CallbackInfoReturnable callbackInfoReturnable) {
        Camera camera = Camera.INSTANCE;
        if (camera == null || !camera.isSet61()) {
            return;
        }
        Matrix4f matrix4f = new Matrix4f().setPerspective((float)Math.toRadians(f), camera.ratio.getFloat35(), 0.05f, this.viewDistanceBlocks * 4.0f);
        callbackInfoReturnable.setReturnValue((Object)matrix4f);
    }

    @Inject(method={"renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V"}, at={@At(value="TAIL")})
    private void m902(RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        Camera camera = Camera.INSTANCE;
        if (camera != null && camera.isSet144()) {
            camera.m885();
        }
    }
}

