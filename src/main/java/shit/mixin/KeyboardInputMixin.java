/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.Client;
import shit.event.InputTickEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={KeyboardInput.class})
public class KeyboardInputMixin {
    @Inject(method={"tick()V"}, at={@At(value="RETURN")})
    private void alien$onInputTick(CallbackInfo callbackInfo) {
        Client.eventBus.m287(new InputTickEvent());
    }
}

