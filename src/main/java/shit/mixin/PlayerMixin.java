/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.Client;
import shit.event.TravelHeadEvent;
import shit.module.client.ClientSetting;
import shit.module.movement.AutoSprint;

@Environment(value=EnvType.CLIENT)
@Mixin(value={PlayerEntity.class})
public class PlayerMixin {
    private boolean shouldMovementSync() {
        boolean bl = ClientSetting.INSTANCE != null && (Boolean)ClientSetting.INSTANCE.movementSync.getObj() != false;
        boolean bl2 = AutoSprint.INSTANCE != null && AutoSprint.INSTANCE.isSet160();
        return (bl || bl2) && Client.mathUtil.isSet111();
    }

    @Inject(method={"travel(Lnet/minecraft/util/math/Vec3d;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$travelHead(Vec3d vec3d, CallbackInfo callbackInfo) {
        Object playerMixin = (Object) this;
        if (!(playerMixin instanceof ClientPlayerEntity)) {
            return;
        }
        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)playerMixin;
        if (this.shouldMovementSync()) {
            Client.mathUtil.setObj37(clientPlayerEntity);
        }
        playerMixin = clientPlayerEntity.getVelocity();
        boolean bl = clientPlayerEntity.input != null && clientPlayerEntity.input.playerInput.jump();
        boolean bl2 = clientPlayerEntity.input != null && clientPlayerEntity.input.playerInput.sneak();
        TravelHeadEvent travelHeadEvent = new TravelHeadEvent(vec3d, bl, bl2, ((Vec3d)playerMixin).x, ((Vec3d)playerMixin).y, ((Vec3d)playerMixin).z);
        Client.eventBus.m287(travelHeadEvent);
        if (travelHeadEvent.isSet85()) {
            clientPlayerEntity.setVelocity(travelHeadEvent.getDouble3(), travelHeadEvent.getDouble5(), travelHeadEvent.getDouble11());
            clientPlayerEntity.move(MovementType.SELF, clientPlayerEntity.getVelocity());
            callbackInfo.cancel();
        }
    }

    @Inject(method={"travel(Lnet/minecraft/util/math/Vec3d;)V"}, at={@At(value="TAIL")})
    private void alien$travelTail(Vec3d vec3d, CallbackInfo callbackInfo) {
        Object playerMixin = (Object) this;
        if (!(playerMixin instanceof ClientPlayerEntity)) {
            return;
        }
        ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)playerMixin;
        if (this.shouldMovementSync()) {
            Client.mathUtil.setObj39(clientPlayerEntity);
        }
    }
}

