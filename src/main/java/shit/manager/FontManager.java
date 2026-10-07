/*
 * Decompiled with CFR 0.152.
 */
package shit.manager;

import java.awt.Font;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import shit.Client;
import shit.manager.FontManager2;
import shit.misc.MathUtil;
import shit.module.Module;
import shit.module.client.Fonts;

@Environment(value=EnvType.CLIENT)
public class FontManager {
    private final Map<String, java.awt.Font> map45 = new ConcurrentHashMap();
    private final Map<String, shit.manager.FontManager2> map13 = new ConcurrentHashMap();

    public void m497() {
        block0: {
            this.m80("default.ttf");
            this.m80("jura-light.ttf");
            this.m80("lexenddeca-regular.ttf");
            this.m80("next/mi-sans-regular.ttf");
            this.m80("next/google-sans-regular.ttf");
            this.m80("next/google-sans-medium.ttf");
            this.m80("next/google-sans-semibold.ttf");
            this.m80("next/google-sans-bold.ttf");
            this.m80("next/harmony.ttf");
            this.m80("next/lira.ttf");
            this.m80("next/regular.otf");
            this.m80("next/regular_medium.otf");
            this.m80("next/regular_semi.otf");
            this.m80("next/regular_bold.otf");
            this.m80("next/minecraft-regular.ttf");
            this.m80("next/minecraft-bold.otf");
            this.m80("next/minecraft-italic.otf");
            if (Module.getTextArray9() != null) break block0;
            MathUtil.setText4("UoqrTb");
        }
    }

    public Font m978(Object object, float f) {
        String string = (String)object;
        float f2 = f;
        Font font = this.map45.computeIfAbsent(string, this::m80);
        Object var6_6 = null;
        if (font == null) {
            return new Font("SansSerif", 0, Math.round(f2));
        }
        return font.deriveFont(f2);
    }

    public FontManager2 renderer2() {
        Fonts fonts = this.getFonts();
        Object var2_2 = null;
        Fonts.Mode mode = fonts == null ? Fonts.Mode.MISANS : (Fonts.Mode)((Object)fonts.mode.getObj());
        float f = fonts == null ? 9.0f : ((Fonts.SizeMode)((Object)fonts.sizeMode.getObj())).m842(fonts.customSize.getFloat35());
        String string = mode.getText50();
        int n = this.m581(f);
        String string3 = mode.name().toLowerCase() + "_" + n;
        return this.map13.computeIfAbsent(string3, string2 -> {
            Font font = this.m978(string, n);
            Font font2 = this.m978("next/mi-sans-regular.ttf", n);
            return new FontManager2(font, font2, mode.name().toLowerCase(), n);
        });
    }

    public FontManager2 renderer(float f) {
        float f2 = f;
        Fonts fonts = this.getFonts();
        Object var4_4 = null;
        Fonts.Mode mode = fonts == null ? Fonts.Mode.MISANS : (Fonts.Mode)((Object)fonts.mode.getObj());
        String string = mode.getText50();
        int n = this.m581(f2);
        String string3 = mode.name().toLowerCase() + "_" + n;
        return this.map13.computeIfAbsent(string3, string2 -> {
            Font font = this.m978(string, n);
            Font font2 = this.m978("next/mi-sans-regular.ttf", n);
            return new FontManager2(font, font2, mode.name().toLowerCase(), n);
        });
    }

    public boolean isSet89() {
        return false;
    }

    public float getFloat47() {
        Fonts fonts = this.getFonts();
        Object var2_2 = null;
        return fonts == null ? 0.0f : fonts.offset.getFloat35();
    }

    private Fonts getFonts() {
        Fonts fonts;
        Object var2_1 = null;
        if (Client.moduleManager == null) {
            return null;
        }
        Module module = Client.moduleManager.m979("Fonts");
        return module instanceof Fonts ? (fonts = (Fonts)module) : null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Font m80(String string) {
        Object var2_2 = null;
        try (InputStream inputStream = FontManager.class.getClassLoader().getResourceAsStream("assets/alien/font/" + string);){
            if (inputStream == null) {
                Font font = null;
                return font;
            }
            Font font = Font.createFont(0, inputStream);
            return font;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private int m581(float f) {
        float f2 = f;
        double d = MinecraftClient.getInstance().getWindow().getScaleFactor();
        return Math.max(8, Math.min(96, Math.round(f2 * (float)Math.max(1.0, d))));
    }
}

