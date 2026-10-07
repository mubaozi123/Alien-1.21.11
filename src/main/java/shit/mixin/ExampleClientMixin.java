/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.Client;
import shit.event.DisconnectEvent;
import shit.event.Event2;
import shit.event.SetScreenEvent;
import shit.event.StartAttackEvent;
import shit.event.StartUseItemEvent;
import shit.module.misc.IRC;
import shit.util.ApiEndpoints3;
import shit.util.AuthUtil;

@Environment(value=EnvType.CLIENT)
@Mixin(value={MinecraftClient.class})
public class ExampleClientMixin {
    @Inject(method={"tick()V"}, at={@At(value="HEAD")})
    private void alien$preTick(CallbackInfo callbackInfo) {
        Client.eventBus.m287(new Event2.Event2Inner());
    }

    @Inject(method={"tick()V"}, at={@At(value="TAIL")})
    private void alien$postTick(CallbackInfo callbackInfo) {
        Client.eventBus.m287(new Event2.Event2Inner2());
    }

    @Inject(method={"setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$setScreen(Screen screen, CallbackInfo callbackInfo) {
        SetScreenEvent setScreenEvent = (SetScreenEvent) Client.eventBus.m287(new SetScreenEvent(screen));
        if (setScreenEvent.isSet85()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"doAttack()Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$startAttack(CallbackInfoReturnable callbackInfoReturnable) {
        MinecraftClient minecraftClient = (MinecraftClient) (Object) this;
        Entity entity = minecraftClient.targetedEntity;
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (IRC.m615(playerEntity.getName().getString())) {
                callbackInfoReturnable.setReturnValue((Object) false);
                return;
            }
        }
        if (((StartAttackEvent) Client.eventBus.m287(new StartAttackEvent())).isSet85()) {
            callbackInfoReturnable.setReturnValue((Object) false);
        }
    }

    @Inject(method={"doItemUse()V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$startUseItem(CallbackInfo callbackInfo) {
        if (!ApiEndpoints3.isSet46()) {
            callbackInfo.cancel();
            return;
        }
        if (((StartUseItemEvent)Client.eventBus.m287(new StartUseItemEvent())).isSet85()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"disconnect(Lnet/minecraft/client/gui/screen/Screen;ZZ)V"}, at={@At(value="HEAD")})
    private void alien$disconnect(Screen screen, boolean bl, boolean bl2, CallbackInfo callbackInfo) {
        Client.eventBus.m287(new DisconnectEvent());
    }

    @Inject(method={"onResolutionChanged()V"}, at={@At(value="TAIL")})
    private void alien$resizeDisplay(CallbackInfo callbackInfo) {
        Client.helper.m27();
    }
}

