/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import shit.Client;

@Environment(value=EnvType.CLIENT)
@Mixin(targets={"net/minecraft/client/render/RenderTickCounter$Dynamic"})
public class TimerMixin {
    @Redirect(method={"beginRenderTick(J)I"}, at=@At(value="INVOKE", target="Lit/unimi/dsi/fastutil/floats/FloatUnaryOperator;apply(F)F"))
    private float alien$modifyMspt(FloatUnaryOperator floatUnaryOperator, float f) {
        float f2 = floatUnaryOperator.apply(f);
        return f2 / Client.helper4.getFloat62();
    }
}

