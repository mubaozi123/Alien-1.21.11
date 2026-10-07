/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(value=EnvType.CLIENT)
@Mixin(value={Screen.class})
public interface ScreenAccessor {
    @Invoker(value="renderPanoramaBackground")
    public void alien$renderPanorama(DrawContext var1, float var2);

    @Invoker(value="clearChildren")
    public void alien$clearWidgets();
}

