/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.PositionInterpolator;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.exploit.Disabler;

@Environment(value=EnvType.CLIENT)
@Mixin(value={PositionInterpolator.class})
public class InterpolationHandlerMixin {
    @Shadow
    public void setLerpDuration(int n) {
        throw new AssertionError((Object)"shadowed");
    }

    @Inject(method={"refreshPositionAndAngles(Lnet/minecraft/util/math/Vec3d;FF)V"}, at={@At(value="TAIL")})
    private void alien$noLerp(Vec3d vec3d, float f, float f2, CallbackInfo callbackInfo) {
        Disabler disabler = Disabler.INSTANCE;
        if (disabler != null && disabler.isSet19() && ((Boolean)disabler.lerp.getObj()).booleanValue()) {
            this.setLerpDuration(1);
        }
    }
}

