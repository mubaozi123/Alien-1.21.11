/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import shit.api.Listener8;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ScreenHandler.class})
public class AbstractContainerMenuMixin
implements Listener8 {
    @Shadow
    @Mutable
    private int revision;

    @Override
    public void alien$setStateId(int n) {
        this.revision = n;
    }
}

