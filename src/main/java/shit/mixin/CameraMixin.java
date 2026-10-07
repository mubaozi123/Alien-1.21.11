/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import shit.module.render.Camera;

@Environment(value=EnvType.CLIENT)
@Mixin(value={net.minecraft.client.render.Camera.class})
public abstract class CameraMixin {
    @Shadow
    protected abstract float clipToSpace(float var1);

    @Inject(method={"clipToSpace(F)F"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$clipDistance(float f, CallbackInfoReturnable callbackInfoReturnable) {
        Camera camera = Camera.INSTANCE;
        if (camera != null && camera.isSet131()) {
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(camera.clipDistance.getFloat35()));
        }
    }

    @ModifyArgs(method={"update(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;ZZF)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;moveBy(FFF)V", ordinal=0))
    private void setArgs(Args args) {
        Camera camera = Camera.INSTANCE;
        if (camera != null && camera.isSet131()) {
            args.set(0, (Object)Float.valueOf(-camera.clipDistance.getFloat35()));
        }
    }

    @ModifyArgs(method={"update(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;ZZF)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void alien$motionCameraPos(Args args) {
        Camera camera = Camera.INSTANCE;
        if (camera != null && camera.isSet74()) {
            args.set(0, (Object)camera.getDouble9());
            args.set(1, (Object)camera.getDouble14());
            args.set(2, (Object)camera.getDouble8());
        }
    }
}

