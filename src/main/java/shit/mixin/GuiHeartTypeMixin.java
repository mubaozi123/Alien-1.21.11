/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={InGameHud.class})
public class GuiHeartTypeMixin {
    @ModifyExpressionValue(method={"renderHealthBar"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/InGameHud$HeartType;fromPlayerState(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/client/gui/hud/InGameHud$HeartType;")})
    @Coerce
    private Object alien$noWitherHeart(@Coerce Object object) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.witherHearts.getObj()).booleanValue() && object != null && object.toString().equals("WITHERED")) {
            for (Object obj : object.getClass().getEnumConstants()) {
                if (!obj.toString().equals("NORMAL")) continue;
                return obj;
            }
        }
        return object;
    }
}

