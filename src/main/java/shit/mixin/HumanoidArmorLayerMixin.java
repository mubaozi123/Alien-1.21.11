/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.render.ArmorHide;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ArmorFeatureRenderer.class})
public class HumanoidArmorLayerMixin {
    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noArmor(MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int n, BipedEntityRenderState bipedEntityRenderState, float f, float f2, CallbackInfo callbackInfo) {
        if (ArmorHide.isSet126() || NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.armorParts.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}

