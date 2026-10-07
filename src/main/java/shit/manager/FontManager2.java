/*
 * Decompiled with CFR 0.152.
 */
package shit.manager;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import shit.module.Module;
import shit.util.MC;

@Environment(value=EnvType.CLIENT)
public class FontManager2
implements MC,
AutoCloseable {
    private final Font font2;
    private final Font font;
    private final String text395;
    private final int count145;
    private final BufferedImage bufferedImage;
    private final Graphics2D field58;
    private final FontMetrics fontMetrics;
    private final FontMetrics fontMetrics2;
    private final int count198;
    private final NativeImage field23;
    private final NativeImageBackedTexture field56;
    private final Identifier field41;
    private final Map map46;
    private static final Map map8 = new java.util.LinkedHashMap<>();
    private int count131;
    private int count166;
    private int count53;
    private boolean flag175;
    private static boolean flag174;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean m414(int n) {
        int n2 = n;
        n2 = Character.toLowerCase((char)n2);
        boolean bl = FontManager2.isSet176();
        int n3 = n2;
        int n4 = 48;
        if (!bl) {
            if (n3 >= n4) {
                n3 = n2;
                if (bl) return n3 != 0;
                if (n3 <= 57) return 1 != 0;
            }
            n3 = n2;
            n4 = 97;
        }
        if (!bl) {
            if (n3 >= n4) {
                n3 = n2;
                if (bl) return n3 != 0;
                if (n3 <= 102) return 1 != 0;
            }
            n3 = n2;
            n4 = 107;
        }
        if (!bl) {
            if (n3 >= n4) {
                n3 = n2;
                if (bl) return n3 != 0;
                if (n3 <= 111) return 1 != 0;
            }
            n3 = n2;
            if (bl) return n3 != 0;
            n4 = 114;
        }
        if (n3 != n4) return 0 != 0;
        return 1 != 0;
    }

    public FontManager2(Font font, Font font2, String string, int n) {
        block1: {
            boolean bl = FontManager2.isSet176();
            boolean bl2 = bl;
            this.map46 = new HashMap();
            this.count131 = 2;
            this.count166 = 2;
            this.font2 = font.deriveFont(0, n);
            Font font3 = font2;
            if (!bl2) {
                font3 = font3 == null ? font : font2;
            }
            this.font = font3.deriveFont(0, n);
            this.text395 = string;
            this.count145 = n;
            this.bufferedImage = new BufferedImage(1024, 1024, 2);
            this.field58 = this.bufferedImage.createGraphics();
            this.field58.setColor(Color.WHITE);
            this.field58.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            this.field58.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            this.field58.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            this.field58.setFont(this.font2);
            this.fontMetrics = this.field58.getFontMetrics(this.font2);
            this.fontMetrics2 = this.field58.getFontMetrics(this.font);
            this.count198 = Math.max(this.fontMetrics.getHeight(), this.fontMetrics2.getHeight());
            this.count53 = this.count198 + 4;
            this.field23 = new NativeImage(1024, 1024, true);
            this.field56 = new NativeImageBackedTexture(() -> "alien font " + string + " " + n, this.field23);
            this.field41 = Identifier.of((String)"alien", (String)("font/runtime/" + string + "_" + n));
            MC.client3.getTextureManager().registerTexture(this.field41, (AbstractTexture)this.field56);
            this.setObj116("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,:;!?/\\_-+[]()<>|");
            this.m424();
            if (!bl2) break block1;
            Module.setTextArray9(new String[4]);
        }
    }

    public void m5(Object object, Object object2, int n, int n2, int n3, boolean bl) {
        boolean bl2;
        int n4;
        int n5;
        int n6;
        String string;
        DrawContext drawContext;
        block6: {
            block5: {
                drawContext = (DrawContext)object;
                string = (String)object2;
                n6 = n;
                n5 = n2;
                n4 = n3;
                bl2 = bl;
                boolean bl3 = true;
                if (string == null) break block5;
                if (!string.isEmpty()) break block6;
            }
            return;
        }
        this.setObj116(string);
        if (this.flag175) {
            this.m424();
        }
        float f = this.getFloat7();
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().scale(1.0f / f);
        if (bl2) {
            int n7 = Math.max(48, (int)((float)(n4 >>> 24 & 0xFF) * 0.38f));
            this.m721(drawContext, string, Math.round(((float)n6 + 0.6f) * f), Math.round(((float)n5 + 0.7f) * f), n7 << 24, true);
        }
        this.m721(drawContext, string, Math.round((float)n6 * f), Math.round((float)n5 * f), n4, false);
        drawContext.getMatrices().popMatrix();
    }

    private void m721(Object object, Object object2, int n, int n2, int n3, boolean c) {
        int n4;
        DrawContext drawContext = (DrawContext)object;
        String string = (String)object2;
        int n5 = n;
        int n6 = n2;
        int n7 = n3;
        int c2 = c ? 1 : 0;
        boolean bl = FontManager2.isSet176();
        int n8 = n7;
        if (!bl) {
            n8 = n8 == 0 ? -1 : n7;
        }
        int n9 = n4 = n8;
        float f = 0.0f;
        float f2 = 0.0f;
        int n10 = 0;
        while (n10 < string.length()) {
            Data data;
            block23: {
                int n11;
                block22: {
                    int n12;
                    block21: {
                        block20: {
                            int n13;
                            int n14;
                            block15: {
                                block17: {
                                    block16: {
                                        Integer n15;
                                        char c3;
                                        block19: {
                                            int n16;
                                            block18: {
                                                n14 = n12 = string.codePointAt(n10);
                                                n13 = 167;
                                                if (bl) break block15;
                                                if (n14 != n13) break block16;
                                                n14 = n10 + 1;
                                                n13 = string.length();
                                                if (bl) break block15;
                                                if (n14 >= n13) break block16;
                                                n14 = FontManager2.m414(string.charAt(n10 + 1)) ? 1 : 0;
                                                if (bl) break block17;
                                                if (n14 == 0) break block16;
                                                c3 = Character.toLowerCase(string.charAt(n10 + 1));
                                                n10 += 2;
                                                n16 = c2;
                                                if (!bl) {
                                                    if (n16 != 0) continue;
                                                    n16 = c3;
                                                }
                                                if (bl) break block18;
                                                if (n16 != 114) break block19;
                                                n16 = n4;
                                            }
                                            n9 = n16;
                                            if (!bl) continue;
                                        }
                                        if ((n15 = (Integer)map8.get(Character.valueOf(c3))) != null) {
                                            n9 = n4 & 0xFF000000 | n15 & 0xFFFFFF;
                                        }
                                        if (!bl) continue;
                                    }
                                    n10 += Character.charCount(n12);
                                    if (bl) break block20;
                                    n14 = n12;
                                }
                                n13 = 10;
                            }
                            if (n14 != n13) break block21;
                            f = 0.0f;
                            f2 += (float)this.getInt19();
                        }
                        if (!bl) continue;
                    }
                    Data data2 = data = (Data)this.map46.get(n12);
                    if (!bl) {
                        if (data2 == null) continue;
                        data2 = data;
                    }
                    n11 = data2.count9;
                    if (bl) break block22;
                    if (n11 <= 0) break block23;
                    n11 = data.count10;
                }
                if (n11 > 0) {
                    drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, this.field41, Math.round((float)n5 + f + (float)data.count11), Math.round((float)n6 + f2 + (float)data.count12), data.value38, data.value39, data.count9, data.count10, 1024, 1024, n9);
                }
            }
            f += data.value40;
            if (!bl) continue;
        }
    }

    public int m277(Object object) {
        String string;
        block12: {
            block11: {
                string = (String)object;
                boolean bl = true;
                if (string == null) break block11;
                if (!string.isEmpty()) break block12;
            }
            return 0;
        }
        this.setObj116(string);
        float f = 0.0f;
        float f2 = 0.0f;
        int n = 0;
        while (n < string.length()) {
            block14: {
                Data data;
                int n2;
                block13: {
                    n2 = string.codePointAt(n);
                    if (n2 == 167) {
                        if (n + 1 < string.length()) {
                            if (FontManager2.m414(string.charAt(n + 1))) {
                                n += 2;
                                if (true) continue;
                            }
                        }
                    }
                    n += Character.charCount(n2);
                    if (n2 != 10) break block13;
                    f = Math.max(f, f2);
                    f2 = 0.0f;
                    if (true) break block14;
                }
                if ((data = (Data)this.map46.get(n2)) != null) {
                    f2 += data.value40;
                }
            }
            if (true) continue;
        }
        return Math.round(Math.max(f, f2) / this.getFloat7());
    }

    public int getInt19() {
        return Math.round((float)this.count198 / this.getFloat7());
    }

    @Override
    public void close() {
        this.field58.dispose();
        boolean bl = FontManager2.isSet176();
        MC.client3.getTextureManager().destroyTexture(this.field41);
        boolean bl2 = bl;
        this.field23.close();
        if (Module.getTextArray9() == null) {
            FontManager2.setFlag13(!bl2);
        }
    }

    private void setObj116(Object object) {
        String string = (String)object;
        int n = 0;
        boolean bl = true;
        while (n < string.length()) {
            int n2 = string.codePointAt(n);
            n += Character.charCount(n2);
            if (n2 != 10) {
                if (!this.map46.containsKey(n2)) {
                    this.setInt5(n2);
                }
            }
            if (true) continue;
        }
    }

    private void setInt5(int n) {
        FontManager2 fontManager2;
        int n2;
        int n3;
        String string;
        FontMetrics fontMetrics;
        int n4;
        block7: {
            int n5;
            int n6;
            Font font;
            block6: {
                n4 = n;
                boolean bl = FontManager2.isSet176();
                if (Character.isWhitespace(n4)) {
                    FontMetrics fontMetrics2 = this.m152(n4);
                    this.map46.put(n4, new Data(0.0f, 0.0f, 0, 0, Math.max(4.0f, (float)fontMetrics2.charWidth(' ')), 0, 0));
                    return;
                }
                font = this.m752(n4);
                fontMetrics = font == this.font ? this.fontMetrics2 : this.fontMetrics;
                string = new String(Character.toChars(n4));
                FontRenderContext fontRenderContext = this.field58.getFontRenderContext();
                Rectangle2D rectangle2D = font.getStringBounds(string, fontRenderContext);
                n3 = Math.max(1, (int)Math.ceil(rectangle2D.getWidth()) + 4);
                n2 = Math.max(1, (int)Math.ceil(rectangle2D.getHeight()) + 4);
                n6 = this.count131 + n3;
                n5 = 1024;
                if (bl) break block6;
                if (n6 >= n5) {
                    this.count131 = 2;
                    this.count166 += this.count53;
                    this.count53 = n2;
                }
                fontManager2 = this;
                if (bl) break block7;
                n6 = fontManager2.count166 + n2;
                n5 = 1024;
            }
            if (n6 >= n5) {
                this.map46.put(n4, new Data(0.0f, 0.0f, 0, 0, Math.max(0.0f, (float)fontMetrics.stringWidth(string)), 0, 0));
                return;
            }
            this.field58.setFont(font);
            this.field58.setColor(Color.WHITE);
            fontManager2 = this;
        }
        fontManager2.field58.drawString(string, this.count131 + 2, this.count166 + 2 + fontMetrics.getAscent());
        float f = (float)fontMetrics.getStringBounds(string, this.field58).getWidth();
        this.map46.put(n4, new Data(this.count131, this.count166, n3, n2, f, -2, -2));
        this.count131 += n3 + 2;
        this.count53 = Math.max(this.count53, n2);
        this.flag175 = true;
    }

    private Font m752(int n) {
        int n2 = n;
        boolean bl = true;
        if (this.font2.canDisplay(n2)) {
            return this.font2;
        }
        if (this.font != null) {
            if (this.font.canDisplay(n2)) {
                return this.font;
            }
        }
        return this.font2;
    }

    private FontMetrics m152(int n) {
        int n2 = n;
        Font font = this.m752(n2);
        return font == this.font ? this.fontMetrics2 : this.fontMetrics;
    }

    private void m424() {
        boolean bl = true;
        for (int i = 0; i < 1024; ++i) {
            for (int j = 0; j < 1024; ++j) {
                this.field23.setColorArgb(j, i, this.bufferedImage.getRGB(j, i));
                if (true) continue;
            }
            if (true) continue;
        }
        this.field56.upload();
        this.flag175 = false;
    }

    private float getFloat7() {
        return Math.max(1.0f, (float)MC.client3.getWindow().getScaleFactor());
    }

    /*
     * Unable to fully structure code
     */
    static {}

    public static void setFlag13(boolean bl) {
        flag174 = bl;
    }

    public static boolean isSet112() {
        return flag174;
    }

    public static boolean isSet176() {
        boolean bl = true;
        return !true;
    }

    @Environment(value=EnvType.CLIENT)
    static final class Data  {
        private final float value38;
        private final float value39;
        private final int count9;
        private final int count10;
        private final float value40;
        private final int count11;
        private final int count12;

        private Data(float f, float f2, int n, int n2, float f3, int n3, int n4) {
            this.value38 = f;
            this.value39 = f2;
            this.count9 = n;
            this.count10 = n2;
            this.value40 = f3;
            this.count11 = n3;
            this.count12 = n4;
        }

        public float value38() {
            return this.value38;
        }

        public float getFloat15() {
            return this.value39;
        }

        public int getInt24() {
            return this.count9;
        }

        public int getInt17() {
            return this.count10;
        }

        public float value40() {
            return this.value40;
        }

        public int count11() {
            return this.count11;
        }

        public int getInt23() {
            return this.count12;
        }
    }
}

