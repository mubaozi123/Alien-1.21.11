/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.state.EndCrystalEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.combat.CrystalChams;

@Environment(value=EnvType.CLIENT)
@Mixin(value={EndCrystalEntityRenderer.class})
public class EndCrystalRendererMixin {
    @Unique
    private static final Identifier field30 = Identifier.ofVanilla((String)"textures/entity/end_crystal/end_crystal.png");
    @Unique
    private boolean alien$crystalChams;

    @Inject(method={"render(Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"}, at={@At(value="HEAD")})
    private void m889(EndCrystalEntityRenderState endCrystalEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo callbackInfo) {
        CrystalChams crystalChams = CrystalChams.INSTANCE;
        this.alien$crystalChams = crystalChams != null && crystalChams.m177(endCrystalEntityRenderState);
    }

    @Inject(method={"render(Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"}, at={@At(value="RETURN")})
    private void m899(EndCrystalEntityRenderState endCrystalEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo callbackInfo) {
        this.alien$crystalChams = false;
    }

    @Redirect(method={"render(Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
    private void alien$scaleCrystal(MatrixStack matrixStack, float f, float f2, float f3) {
        CrystalChams crystalChams = CrystalChams.INSTANCE;
        if (this.alien$crystalChams && crystalChams != null) {
            float f4 = crystalChams.getFloat9();
            matrixStack.scale(f * f4, f2 * f4, f3 * f4);
        } else {
            matrixStack.scale(f, f2, f3);
        }
    }

    @Redirect(method={"render(Lnet/minecraft/client/render/entity/state/EndCrystalEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"))
    private void m907(OrderedRenderCommandQueue orderedRenderCommandQueue, Model model, Object object, MatrixStack matrixStack, RenderLayer renderLayer, int n, int n2, int n3, ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        CrystalChams crystalChams = CrystalChams.INSTANCE;
        if (this.alien$crystalChams && crystalChams != null) {
            crystalChams.m411(object, model);
            if (ColorHelper.getAlpha((int)crystalChams.getInt65()) < 255) {
                renderLayer = RenderLayers.entityTranslucent((Identifier)field30);
            }
            orderedRenderCommandQueue.submitModel(model, object, matrixStack, renderLayer, n, n2, crystalChams.getInt65(), null, n3, crumblingOverlayCommand);
        } else {
            orderedRenderCommandQueue.submitModel(model, object, matrixStack, renderLayer, n, n2, n3, crumblingOverlayCommand);
        }
    }
}

