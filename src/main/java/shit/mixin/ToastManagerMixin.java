/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ToastManager.class})
public class ToastManagerMixin {
    @Inject(method={"add(Lnet/minecraft/client/toast/Toast;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noToast(Toast toast, CallbackInfo callbackInfo) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.guiToast.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}

