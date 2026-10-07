/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.render.AntiOverlay;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={InGameOverlayRenderer.class})
public class ScreenEffectRendererMixin {
    @Inject(method={"renderFireOverlay(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/texture/Sprite;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private static void alien$noFireOverlay(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Sprite sprite, CallbackInfo callbackInfo) {
        if (AntiOverlay.isSet175() || NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.fireOverlay.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderUnderwaterOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private static void alien$noWaterOverlay(MinecraftClient minecraftClient, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, CallbackInfo callbackInfo) {
        if (AntiOverlay.isSet147() || NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.waterOverlay.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderInWallOverlay(Lnet/minecraft/client/texture/Sprite;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private static void alien$noBlockOverlay(Sprite sprite, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, CallbackInfo callbackInfo) {
        if (AntiOverlay.isSet28() || NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.blockOverlay.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}

