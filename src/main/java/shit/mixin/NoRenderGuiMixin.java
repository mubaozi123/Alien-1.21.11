/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={InGameHud.class})
public class NoRenderGuiMixin {
    @Inject(method={"renderStatusEffectOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noPotionsIcon(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.potionsIcon.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderPortalOverlay(Lnet/minecraft/client/gui/DrawContext;F)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noPortal(DrawContext drawContext, float f, CallbackInfo callbackInfo) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.portal.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderNauseaOverlay(Lnet/minecraft/client/gui/DrawContext;F)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$noNausea(DrawContext drawContext, float f, CallbackInfo callbackInfo) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.nausea.getObj()).booleanValue()) {
            callbackInfo.cancel();
        }
    }
}

