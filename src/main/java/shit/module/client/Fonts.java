/*
 * Decompiled with CFR 0.152.
 */
package shit.module.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import shit.module.Category;
import shit.module.Module;
import shit.module.client.ClientSetting;
import shit.setting.EnumSetting;
import shit.setting.NumberSetting;

@Environment(value=EnvType.CLIENT)
public class Fonts
extends Module {
    public final EnumSetting mode = (EnumSetting)this.m28(new EnumSetting("Mode", Mode.ALIEN_LEXEND));
    public final EnumSetting sizeMode = (EnumSetting)this.m28(new EnumSetting("SizeMode", SizeMode.NORMAL));
    public final NumberSetting customSize = (NumberSetting)this.m28(new NumberSetting("CustomSize", 9.0, 6.0, 18.0, 1.0, 1.0, () -> this.sizeMode.getObj() == SizeMode.CUSTOM, null, "", false));
    public final NumberSetting offset = (NumberSetting)this.m28(new NumberSetting("Offset", 0.0, -5.0, 5.0, 0.5));

    public Fonts() {
        super("Fonts", "Controls font preferences.", Category.CLIENT);
    }

    private static MatchException a(MatchException matchException) {
        return matchException;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum SizeMode {
      SMALL(8.0f), NORMAL(9.0f), LARGE(11.0f), CUSTOM(-1.0f);

      private SizeMode(float value127) { this.value127 = value127; }


        private final float value127;

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */

        /*
         * Enabled aggressive block sorting
         */
        public float m842(float f) {
            float f2;
            float f3 = f;
            int[] nArray = ClientSetting.getIntArray();
            SizeMode sizeMode = this;
            if (nArray != null) {
                if (sizeMode == CUSTOM) {
                    f2 = f3;
                    return f2;
                }
                sizeMode = this;
            }
            f2 = sizeMode.value127;
            return f2;
        }

        private static SizeMode[] getSizeModeArray() {
            return new SizeMode[]{SMALL, NORMAL, LARGE, CUSTOM};
        }
    
   }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        ALIEN_JURA("jura-light.ttf"), ALIEN_LEXEND("lexenddeca-regular.ttf"), MISANS("next/mi-sans-regular.ttf"),
        GOOGLE_SANS("next/google-sans-regular.ttf"), GOOGLE_SANS_MEDIUM("next/google-sans-medium.ttf"),
        GOOGLE_SANS_SEMI_BOLD("next/google-sans-semibold.ttf"), GOOGLE_SANS_BOLD("next/google-sans-bold.ttf"),
        HARMONY("next/harmony.ttf"), LIRA("next/lira.ttf"), REGULAR("next/regular.otf"),
        REGULAR_MEDIUM("next/regular_medium.otf"), REGULAR_SEMI("next/regular_semi.otf"),
        REGULAR_BOLD("next/regular_bold.otf"), MINECRAFT_REGULAR("next/minecraft-regular.ttf"),
        MINECRAFT_BOLD("next/minecraft-bold.otf"), MINECRAFT_ITALIC("next/minecraft-italic.otf");

        private final String text2624;

        private Mode(String font) { this.text2624 = font; }

        public String getText50() {
            return this.text2624;
        }
    }
}

