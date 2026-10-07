/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import shit.module.render.Chams;

@Environment(value=EnvType.CLIENT)
@Mixin(value={PlayerEntityRenderer.class})
public class AvatarRendererChamsMixin {
    @Redirect(method={"renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;Lnet/minecraft/client/model/ModelPart;Z)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModelPart(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IILnet/minecraft/client/texture/Sprite;)V"))
    private void alien$submitChamsHand(OrderedRenderCommandQueue orderedRenderCommandQueue, ModelPart modelPart, MatrixStack matrixStack, RenderLayer renderLayer, int n, int n2, Sprite sprite) {
        Chams chams = Chams.INSTANCE;
        if (chams != null && chams.isSet155()) {
            orderedRenderCommandQueue.submitModelPart(modelPart, matrixStack, renderLayer, n, n2, sprite, false, false, chams.getInt11(), null, 0);
        } else {
            orderedRenderCommandQueue.submitModelPart(modelPart, matrixStack, renderLayer, n, n2, sprite);
        }
    }
}

