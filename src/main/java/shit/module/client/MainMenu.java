/*
 * Decompiled with CFR 0.152.
 */
package shit.module.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import shit.manager.ShaderProgramManager;
import shit.module.Category;
import shit.module.Module;
import shit.module.client.ClientSetting;
import shit.setting.BooleanSetting;
import shit.setting.EnumSetting;
import shit.setting.NumberSetting;

@Environment(value=EnvType.CLIENT)
public class MainMenu
extends Module {
    public static MainMenu INSTANCE;
    private final BooleanSetting shader = (BooleanSetting)this.m28(new BooleanSetting("Shader", true));
    private final BooleanSetting customButtons = (BooleanSetting)this.m28(new BooleanSetting("CustomButtons", true));
    private final EnumSetting title = (EnumSetting)this.m28(new EnumSetting("Title", EMode.TROLL_HACK));
    private final EnumSetting mode = (EnumSetting)this.m28(new EnumSetting("Mode", Mode.SET));
    private final EnumSetting backgroundShader = (EnumSetting)this.m28(new EnumSetting("BackgroundShader", ShaderProgramManager.EMode.eMode));
    private final NumberSetting fpsLimit = (NumberSetting)this.m28(new NumberSetting("FpsLimit", 60.0, 10.0, 240.0, 10.0));
    private ShaderProgramManager.EMode field13 = null;

    public MainMenu() {
        super("MainMenu", "1122-style shader main menu background.", Category.CLIENT);
        INSTANCE = this;
        this.setFlag3(true);
    }

    public boolean isSet9() {
        return (Boolean)this.shader.getObj();
    }

    public boolean isSet110() {
        return (Boolean)this.customButtons.getObj();
    }

    public void m590() {
        block4: {
            ShaderProgramManager.EMode eMode;
            block5: {
                ShaderProgramManager.EMode eMode2;
                block6: {
                    int[] nArray = ClientSetting.getIntArray();
                    if (this.mode.getObj() != Mode.RANDOM) {
                        return;
                    }
                    ShaderProgramManager.EMode[] eModeArray = ShaderProgramManager.EMode.values();
                    eMode = eModeArray[(int)(Math.random() * (double)eModeArray.length)];
                    if (nArray == null) break block4;
                    if (eModeArray.length <= 1) break block5;
                    eMode2 = eMode;
                    if (nArray == null) break block6;
                    if (eMode2 != this.field13) break block5;
                    eMode2 = eModeArray[((int)(Math.random() * (double)eModeArray.length) + 1) % eModeArray.length];
                }
                eMode = eMode2;
            }
            this.field13 = eMode;
            ShaderProgramManager.m884();
        }
    }

    public ShaderProgramManager.EMode getObj8() {
        int[] nArray = ClientSetting.getIntArray();
        Object object = this.mode.getObj();
        if (nArray != null) {
            if (object == Mode.RANDOM) {
                ShaderProgramManager.EMode eMode = this.field13;
                if (nArray != null) {
                    if (eMode == null) {
                        this.m590();
                    }
                    eMode = this.field13;
                }
                return eMode;
            }
            object = this.backgroundShader.getObj();
        }
        return (ShaderProgramManager.EMode)((Object)object);
    }

    public String getText45() {
        return ((EMode)((Object)this.title.getObj())).displayName;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum EMode  {
        TROLL_HACK("Alien"), MINECRAFT("Minecraft");

        public final String displayName;

        private EMode(String displayName) {
            this.displayName = displayName;
        }

        

        /*
         * Unable to fully structure code
         */
        
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
      RANDOM, SET;

      private Mode() {}



        private static Mode[] getModeArray22() {
            return new Mode[]{RANDOM, SET};
        }
    
   }
}

