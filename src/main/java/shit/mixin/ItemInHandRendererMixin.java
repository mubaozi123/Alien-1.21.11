/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.render.ViewModel;

@Environment(value=EnvType.CLIENT)
@Mixin(value={HeldItemRenderer.class})
public class ItemInHandRendererMixin {
    @Shadow
    @Mutable
    private ItemStack mainHand;
    @Shadow
    @Mutable
    private ItemStack offHand;
    @Shadow
    @Mutable
    private float equipProgressMainHand;
    @Shadow
    @Mutable
    private float lastEquipProgressMainHand;
    @Shadow
    @Mutable
    private float equipProgressOffHand;
    @Shadow
    @Mutable
    private float lastEquipProgressOffHand;
    @Unique
    private static final ThreadLocal alien$capturedHand = new ThreadLocal();

    @Inject(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at={@At(value="HEAD")})
    private void alien$instantSwap(float f, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, ClientPlayerEntity clientPlayerEntity, int n, CallbackInfo callbackInfo) {
        ViewModel viewModel = ViewModel.INSTANCE;
        if (viewModel == null || !viewModel.isSet19()) {
            return;
        }
        if (!((Boolean)viewModel.mainhandSwap.getObj()).booleanValue()) {
            this.mainHand = clientPlayerEntity.getMainHandStack();
            this.equipProgressMainHand = 1.0f;
            this.lastEquipProgressMainHand = 1.0f;
        }
        if (!((Boolean)viewModel.offhandSwap.getObj()).booleanValue()) {
            this.offHand = clientPlayerEntity.getOffHandStack();
            this.equipProgressOffHand = 1.0f;
            this.lastEquipProgressOffHand = 1.0f;
        }
    }

    @Inject(method={"renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V"}, at={@At(value="HEAD")})
    private void alien$captureHand(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float f2, Hand hand, float f3, ItemStack itemStack, float f4, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int n, CallbackInfo callbackInfo) {
        alien$capturedHand.set(hand);
    }

    @Inject(method={"renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V"}, at={@At(value="HEAD")})
    private void alien$transform(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int n, CallbackInfo callbackInfo) {
        ViewModel viewModel = ViewModel.INSTANCE;
        if (viewModel == null || !viewModel.isSet19()) {
            return;
        }
        Hand hand = (Hand)alien$capturedHand.get();
        if (hand == null) {
            return;
        }
        if (hand == Hand.MAIN_HAND) {
            matrixStack.translate(viewModel.positionMainX.getFloat35(), viewModel.positionMainY.getFloat35(), viewModel.positionMainZ.getFloat35());
            matrixStack.scale(viewModel.scaleMainX.getFloat35(), viewModel.scaleMainY.getFloat35(), viewModel.scaleMainZ.getFloat35());
            matrixStack.multiply((Quaternionfc)RotationAxis.POSITIVE_X.rotationDegrees(viewModel.rotationMainX.getFloat35()));
            matrixStack.multiply((Quaternionfc)RotationAxis.POSITIVE_Y.rotationDegrees(viewModel.rotationMainY.getFloat35()));
            matrixStack.multiply((Quaternionfc)RotationAxis.POSITIVE_Z.rotationDegrees(viewModel.rotationMainZ.getFloat35()));
        } else {
            matrixStack.translate(viewModel.positionOffX.getFloat35(), viewModel.positionOffY.getFloat35(), viewModel.positionOffZ.getFloat35());
            matrixStack.scale(viewModel.scaleOffX.getFloat35(), viewModel.scaleOffY.getFloat35(), viewModel.scaleOffZ.getFloat35());
            matrixStack.multiply((Quaternionfc)RotationAxis.POSITIVE_X.rotationDegrees(viewModel.rotationOffX.getFloat35()));
            matrixStack.multiply((Quaternionfc)RotationAxis.POSITIVE_Y.rotationDegrees(viewModel.rotationOffY.getFloat35()));
            matrixStack.multiply((Quaternionfc)RotationAxis.POSITIVE_Z.rotationDegrees(viewModel.rotationOffZ.getFloat35()));
        }
    }

    @Redirect(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"))
    private Quaternionf alien$noSway(RotationAxis rotationAxis, float f) {
        ViewModel viewModel = ViewModel.INSTANCE;
        if (viewModel != null && viewModel.isSet19() && ((Boolean)viewModel.noSway.getObj()).booleanValue()) {
            return new Quaternionf();
        }
        return rotationAxis.rotationDegrees(f);
    }
}

