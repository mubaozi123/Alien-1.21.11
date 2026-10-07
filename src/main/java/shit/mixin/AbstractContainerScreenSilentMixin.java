/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.combat.AntiRegear;
import shit.module.combat.AutoRegear;

@Environment(value=EnvType.CLIENT)
@Mixin(value={HandledScreen.class})
public class AbstractContainerScreenSilentMixin {
    @Inject(method={"render(Lnet/minecraft/client/gui/DrawContext;IIF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$cancelRender(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        if (AutoRegear.isSet128() || AntiRegear.isSet81()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void m888(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        if (AutoRegear.isSet128() || AntiRegear.isSet81()) {
            callbackInfo.cancel();
        }
    }
}

