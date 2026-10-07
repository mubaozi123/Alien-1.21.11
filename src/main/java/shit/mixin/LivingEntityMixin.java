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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.Client;
import shit.api.Listener4;
import shit.module.movement.InMove;
import shit.module.movement.Velocity;
import shit.module.player.AntiEffects;

@Environment(value=EnvType.CLIENT)
@Mixin(value={LivingEntity.class})
public abstract class LivingEntityMixin
implements Listener4 {
    @Inject(method={"hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$onHasEffect(RegistryEntry registryEntry, CallbackInfoReturnable callbackInfoReturnable) {
        AntiEffects antiEffects = AntiEffects.INSTANCE;
        if (antiEffects != null && antiEffects.isSet19() && (Object) this instanceof ClientPlayerEntity) {
            if (registryEntry.value() == StatusEffects.SLOW_FALLING.value() && ((Boolean)antiEffects.slowFalling.getObj()).booleanValue()) {
                callbackInfoReturnable.setReturnValue((Object)false);
            } else if (registryEntry.value() == StatusEffects.LEVITATION.value() && ((Boolean)antiEffects.levitation.getObj()).booleanValue()) {
                callbackInfoReturnable.setReturnValue((Object)false);
            }
        }
    }

    @Inject(method={"jump()V"}, at={@At(value="HEAD")})
    private void alien$jumpSyncPre(CallbackInfo callbackInfo) {
        Object livingEntityMixin = (Object) this;
        if (livingEntityMixin instanceof ClientPlayerEntity) {
            ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)livingEntityMixin;
            if (Client.mathUtil.isSet111()) {
                Client.mathUtil.setObj37(clientPlayerEntity);
            }
        }
    }

    @Inject(method={"jump()V"}, at={@At(value="TAIL")})
    private void alien$jumpSyncPost(CallbackInfo callbackInfo) {
        Object livingEntityMixin = (Object) this;
        if (livingEntityMixin instanceof ClientPlayerEntity) {
            ClientPlayerEntity clientPlayerEntity = (ClientPlayerEntity)livingEntityMixin;
            if (Client.mathUtil.isSet111()) {
                Client.mathUtil.setObj39(clientPlayerEntity);
            }
        }
    }

    @Inject(method={"setSprinting(Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$onSetSprinting(boolean bl, CallbackInfo callbackInfo) {
        if ((Object) this instanceof ClientPlayerEntity && !bl && InMove.INSTANCE != null && InMove.INSTANCE.isSet19() && MinecraftClient.getInstance().currentScreen != null && this.isSet159()) {
            callbackInfo.cancel();
        }
    }

    @Override
    @Invoker(value="tryUseDeathProtector")
    public abstract boolean m642(DamageSource var1);

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

    @Inject(method={"isClimbing()Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noClimb(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        Velocity velocity = Velocity.INSTANCE;
        if ((Object) this instanceof ClientPlayerEntity && velocity != null && velocity.isSet19() && (Boolean) velocity.noClimb.getObj()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}

