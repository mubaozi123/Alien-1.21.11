/*
 * Decompiled with CFR 0.152.
 */
package shit.module.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import shit.event.Event2;
import shit.event.EventHandler;
import shit.mixin.OptionInstanceAccessor;
import shit.module.Category;
import shit.module.Module;
import shit.module.misc.IRC;
import shit.setting.NumberSetting;
import shit.util.MC;

@Environment(value=EnvType.CLIENT)
public class UnfocusedFps
extends Module {
    private final NumberSetting fPS = (NumberSetting)this.m28(new NumberSetting("FPS", 30.0, 1.0, 240.0, 1.0));
    private Integer integer;

    public UnfocusedFps() {
        super("UnfocusedFps", "Lowers the FPS limit when Minecraft is unfocused.", Category.MISC);
    }

    @Override
    public void m709() {
        this.m1040();
    }

    @EventHandler
    private void setEvent2Inner216(Event2.Event2Inner2 event2Inner2) {
        if (MC.client3.getWindow() == null) {
            return;
        }
        SimpleOption simpleOption = MC.client3.options.getMaxFps();
        if (!MC.client3.isWindowFocused()) {
            if (this.integer == null) {
                this.integer = (Integer)simpleOption.getValue();
            }
            ((OptionInstanceAccessor)(Object)simpleOption).alien$set(this.fPS.getInt50());
        } else {
            this.m1040();
        }
    }

    private void m1040() {
        GameOptions gameOptions;
        block3: {
            block4: {
                block2: {
                    String string = IRC.getText7();
                    if (this.integer == null) break block2;
                    gameOptions = MC.client3.options;
                    if (string == null) break block3;
                    if (gameOptions != null) break block4;
                }
                return;
            }
            gameOptions = MC.client3.options;
        }
        ((OptionInstanceAccessor)(Object)gameOptions.getMaxFps()).alien$set(this.integer);
        this.integer = null;
    }
}

