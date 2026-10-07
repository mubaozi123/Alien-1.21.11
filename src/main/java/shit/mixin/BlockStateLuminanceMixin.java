/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.module.render.Ambience;

@Environment(value=EnvType.CLIENT)
@Mixin(value={AbstractBlock.AbstractBlockState.class})
public class BlockStateLuminanceMixin {
    @Inject(method={"getLuminance()I"}, at={@At(value="RETURN")}, cancellable=true)
    private void alien$customLuminance(CallbackInfoReturnable callbackInfoReturnable) {
        Ambience ambience = Ambience.INSTANCE;
        if (ambience != null && ambience.isSet19() && ((Boolean)ambience.customLuminance.getObj()).booleanValue()) {
            callbackInfoReturnable.setReturnValue((Object)ambience.luminance.getInt50());
        }
    }
}

