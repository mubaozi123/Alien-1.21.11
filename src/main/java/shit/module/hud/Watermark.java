/*
 * Decompiled with CFR 0.152.
 */
package shit.module.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import shit.Client;
import shit.api.Listener3;
import shit.mixin.MinecraftAccessor;
import shit.module.Category;
import shit.module.Module;
import shit.setting.BooleanSetting;
import shit.setting.ColorSetting;
import shit.setting.EnumSetting;
import shit.setting.NumberSetting;
import shit.setting.StringSetting;

@Environment(value=EnvType.CLIENT)
public class Watermark
extends Module
implements Listener3 {
    private final StringSetting text = (StringSetting)this.m28(new StringSetting("Text", "Alien"));
    private final EnumSetting mode = (EnumSetting)this.m28(new EnumSetting("Mode", Mode.CLIENT_VERSION));
    private final BooleanSetting info = (BooleanSetting)this.m28(new BooleanSetting("Info", false));
    private final BooleanSetting shadow = (BooleanSetting)this.m28(new BooleanSetting("Shadow", true));
    private final ColorSetting color = (ColorSetting)this.m28(new ColorSetting("Color", -1));
    private final NumberSetting x = (NumberSetting)this.m28(new NumberSetting("X", 6.0, 0.0, 5000.0, 1.0, 1.0, () -> false, null, "", false));
    private final NumberSetting y = (NumberSetting)this.m28(new NumberSetting("Y", 6.0, 0.0, 5000.0, 1.0, 1.0, () -> false, null, "", false));

    public Watermark() {
        super("Watermark", "Draws the client name.", Category.HUD);
        this.setFlag3(true);
    }

    @Override
    public int getInt12() {
        return this.x.getInt50();
    }

    @Override
    public int getInt5() {
        return this.y.getInt50();
    }

    @Override
    public int hudWidth() {
        return Client.fontManager.renderer2().m277(this.getText62());
    }

    @Override
    public int getInt28() {
        return Client.fontManager.renderer2().getInt19();
    }

    @Override
    public void m274(int n, int n2) {
        int n3 = n;
        int n4 = n2;
        this.x.setObj85(n3);
        this.y.setObj85(n4);
    }

    @Override
    public void m368(Object object, boolean bl) {
        DrawContext drawContext = (DrawContext)object;
        Client.fontManager.renderer2().m5(drawContext, this.getText62(), this.x.getInt50(), this.y.getInt50(), (Integer)this.color.getObj(), (Boolean)this.shadow.getObj());
    }

    private String getText62() {
        String string = "";
        switch (((Mode)((Object)this.mode.getObj())).ordinal()) {
            default: {
                throw new MatchException(null, null);
            }
            case 0: {
                String string2 = (String)this.text.getObj();
                break;
            }
            case 1: {
                String string2 = "Alien";
                break;
            }
            case 2: {
                String string2 = string = "Alien 1.0.0";
            }
        }
        if (!((Boolean)this.info.getObj()).booleanValue()) {
            return string;
        }
        return string + " | " + MinecraftAccessor.alien$getFps() + " FPS";
    }

    private static MatchException a(MatchException matchException) {
        return matchException;
    }

    @Environment(value=EnvType.CLIENT)
    static enum Mode {
      CUSTOM, CLIENT, CLIENT_VERSION;

      private Mode() {}



        private static Mode[] getModeArray12() {
            return new Mode[]{CUSTOM, CLIENT, CLIENT_VERSION};
        }
    
   }
}

