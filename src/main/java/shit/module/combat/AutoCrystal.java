/*
 * Placeholder module — implementation code will be filled in later.
 */
package shit.module.combat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import shit.module.Category;
import shit.module.Module;

@Environment(value=EnvType.CLIENT)
public class AutoCrystal
extends Module {
    public static AutoCrystal INSTANCE;

    public AutoCrystal() {
        super("AutoCrystal", "AutoCrystal placeholder.", Category.COMBAT);
        INSTANCE = this;
    }
}
