/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseEffectsComponent;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.Client;
import shit.event.MoveEvent;
import shit.module.movement.InMove;
import shit.module.movement.NoSlow;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ClientPlayerEntity.class})
public class LocalPlayerMixin {
    @Inject(method={"sendMovementPackets()V"}, at={@At(value="HEAD")})
    private void alien$sendPositionPre(CallbackInfo callbackInfo) {
        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)(Object)this;
        if (Client.mathUtil.isSet111()) {
            Client.mathUtil.setObj37(clientPlayerEntity);
        }
    }

    @Inject(method={"sendMovementPackets()V"}, at={@At(value="TAIL")})
    private void setCallbackInfo3(CallbackInfo callbackInfo) {
        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)(Object)this;
        if (Client.mathUtil.isSet111()) {
            Client.mathUtil.setObj39(clientPlayerEntity);
        }
    }

    @Redirect(method={"applyMovementSpeedFactors(Lnet/minecraft/util/math/Vec2f;)Lnet/minecraft/util/math/Vec2f;"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getActiveItemSpeedMultiplier()F"))
    private float m894(ClientPlayerEntity clientPlayerEntity) {
        if (NoSlow.INSTANCE != null && NoSlow.INSTANCE.isSet64()) {
            return 1.0f;
        }
        return ((UseEffectsComponent)clientPlayerEntity.getActiveItem().getOrDefault(DataComponentTypes.USE_EFFECTS, (Object)UseEffectsComponent.DEFAULT)).speedMultiplier();
    }

    @Redirect(method={"applyMovementSpeedFactors(Lnet/minecraft/util/math/Vec2f;)Lnet/minecraft/util/math/Vec2f;"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D"))
    private double m895(ClientPlayerEntity clientPlayerEntity, RegistryEntry registryEntry) {
        double d = clientPlayerEntity.getAttributeValue(registryEntry);
        if (registryEntry.equals((Object)EntityAttributes.SNEAKING_SPEED) && NoSlow.INSTANCE != null && NoSlow.INSTANCE.isSet72()) {
            return 1.0;
        }
        return d;
    }

    @Inject(method={"shouldStopSprinting()Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void setCallbackInfoReturnable6(CallbackInfoReturnable callbackInfoReturnable) {
        if (InMove.INSTANCE != null && InMove.INSTANCE.isSet19() && MinecraftClient.getInstance().currentScreen != null && this.isSet159()) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @ModifyVariable(method={"move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"}, at=@At(value="HEAD"), argsOnly=true)
    private Vec3d alien$onMove(Vec3d vec3d, MovementType movementType) {
        MoveEvent moveEvent = new MoveEvent(vec3d.x, vec3d.y, vec3d.z);
        Client.eventBus.m287(moveEvent);
        if (moveEvent.isSet85()) {
            return Vec3d.ZERO;
        }
        if (moveEvent.flag112) {
            return new Vec3d(moveEvent.getDouble13(), moveEvent.getDouble12(), moveEvent.getDouble2());
        }
        return vec3d;
    }

    @Unique
    private boolean isSet159() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getWindow() == null) {
            return false;
        }
        int n = minecraftClient.options.forwardKey.getDefaultKey().getCode();
        int n2 = minecraftClient.options.backKey.getDefaultKey().getCode();
        int n3 = minecraftClient.options.leftKey.getDefaultKey().getCode();
        int n4 = minecraftClient.options.rightKey.getDefaultKey().getCode();
        return n != -1 && InputUtil.isKeyPressed((Window)minecraftClient.getWindow(), (int)n) || n2 != -1 && InputUtil.isKeyPressed((Window)minecraftClient.getWindow(), (int)n2) || n3 != -1 && InputUtil.isKeyPressed((Window)minecraftClient.getWindow(), (int)n3) || n4 != -1 && InputUtil.isKeyPressed((Window)minecraftClient.getWindow(), (int)n4);
    }
}

