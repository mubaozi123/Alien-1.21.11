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
import shit.Client;
import shit.event.Render2DEvent;
import shit.util.RenderUtil;

@Environment(value=EnvType.CLIENT)
@Mixin(value={InGameHud.class})
public class GuiMixin {
    @Inject(method={"render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"}, at={@At(value="TAIL")})
    private void alien$render2D(DrawContext drawContext, RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        Client.helper.m818();
        Client.eventBus.m287(new Render2DEvent(drawContext, renderTickCounter));
        RenderUtil.setObj31(drawContext);
    }
}

