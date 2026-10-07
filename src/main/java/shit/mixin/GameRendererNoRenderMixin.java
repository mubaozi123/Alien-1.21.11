/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={GameRenderer.class})
public class GameRendererNoRenderMixin {
    @Inject(method={"tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noHurtCam(MatrixStack matrixStack, float f, CallbackInfo callbackInfo) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.hurtCam.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"showFloatingItem(Lnet/minecraft/item/ItemStack;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noTotem(ItemStack itemStack, CallbackInfo callbackInfo) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.totem.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}

