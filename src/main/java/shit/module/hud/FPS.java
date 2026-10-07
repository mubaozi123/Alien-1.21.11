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
import shit.setting.NumberSetting;

@Environment(value=EnvType.CLIENT)
public class FPS
extends Module
implements Listener3 {
    private final NumberSetting x = (NumberSetting)this.m28(new NumberSetting("X", 6.0, 0.0, 5000.0, 1.0, 1.0, () -> false, null, "", false));
    private final NumberSetting y = (NumberSetting)this.m28(new NumberSetting("Y", 30.0, 0.0, 5000.0, 1.0, 1.0, () -> false, null, "", false));
    private final BooleanSetting shadow = (BooleanSetting)this.m28(new BooleanSetting("Shadow", true));
    private final ColorSetting color = (ColorSetting)this.m28(new ColorSetting("Color", -1184275));

    public FPS() {
        super("FPS", "Shows current FPS.", Category.HUD);
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
        return Client.fontManager.renderer2().m277(this.getText9());
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
        Client.fontManager.renderer2().m5(drawContext, this.getText9(), this.x.getInt50(), this.y.getInt50(), (Integer)this.color.getObj(), (Boolean)this.shadow.getObj());
    }

    private String getText9() {
        return "FPS " + MinecraftAccessor.alien$getFps();
    }
}

