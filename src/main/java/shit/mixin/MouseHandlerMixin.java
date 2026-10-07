/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.Client;
import shit.module.Module;
import shit.setting.ColorSetting2;

@Environment(value=EnvType.CLIENT)
@Mixin(value={Mouse.class})
public class MouseHandlerMixin {
    @Inject(method={"onMouseButton(JLnet/minecraft/client/input/MouseInput;I)V"}, at={@At(value="HEAD")})
    private void alien$onMousePress(long l, MouseInput mouseInput, int n, CallbackInfo callbackInfo) {
        int n2 = ColorSetting2.m559(mouseInput.button());
        for (Module module : Client.moduleManager.getList6()) {
            ColorSetting2 colorSetting2 = module.getColorSetting2();
            if ((Integer)colorSetting2.getObj() != n2) continue;
            if (n == 1) {
                if (colorSetting2.getType() == ColorSetting2.Type.Toggle) {
                    module.m84();
                    continue;
                }
                if (colorSetting2.getType() != ColorSetting2.Type.Hold) continue;
                module.setFlag3(true);
                continue;
            }
            if (n != 0 || colorSetting2.getType() != ColorSetting2.Type.Hold) continue;
            module.setFlag3(false);
        }
    }
}

