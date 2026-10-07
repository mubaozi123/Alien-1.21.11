/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.module.movement.NoSlow;
import shit.module.movement.Velocity;

@Environment(value=EnvType.CLIENT)
@Mixin(value={Entity.class})
public class EntityMixin {
    @Inject(method={"getVelocityMultiplier()F"}, at={@At(value="RETURN")}, cancellable=true)
    private void setCallbackInfoReturnable2(CallbackInfoReturnable callbackInfoReturnable) {
        if ((Object) this instanceof ClientPlayerEntity && NoSlow.INSTANCE != null && NoSlow.INSTANCE.isSet87()) {
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(1.0f));
        }
    }

    @Inject(method={"pushAwayFrom(Lnet/minecraft/entity/Entity;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noEntityPush(Entity entity, CallbackInfo callbackInfo) {
        Velocity velocity = Velocity.INSTANCE;
        if ((Object) this instanceof ClientPlayerEntity && velocity != null && velocity.isSet19() && (Boolean) velocity.noEntityPush.getObj()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"pushOutOfBlocks(DDD)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noBlockPush(double d, double d2, double d3, CallbackInfo callbackInfo) {
        Velocity velocity = Velocity.INSTANCE;
        if ((Object) this instanceof ClientPlayerEntity && velocity != null && velocity.isSet19() && (Boolean) velocity.noBlockPush.getObj()) {
            callbackInfo.cancel();
        }
    }

    @Redirect(method={"updateMovementInFluid(Lnet/minecraft/registry/tag/TagKey;D)Z"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void alien$noWaterPush(Entity self, Vec3d vec3d) {
        Velocity velocity = Velocity.INSTANCE;
        if ((Object) this instanceof ClientPlayerEntity && velocity != null && velocity.isSet19() && (Boolean) velocity.noWaterPush.getObj()) {
            return;
        }
        self.setVelocity(vec3d);
    }
}

