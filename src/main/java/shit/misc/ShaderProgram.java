/*
 * Decompiled with CFR 0.152.
 */
package shit.misc;

import com.mojang.blaze3d.opengl.GlStateManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20C;
import shit.module.Module;
import shit.util.Pos;

@Environment(value=EnvType.CLIENT)
public class ShaderProgram {
    private final int count115;

    public ShaderProgram(String string, String string2) {
        block9: {
            String string3 = Pos.getText67();
            String string4 = string3;
            int n = GlStateManager.glCreateShader((int)35633);
            int n2 = GlStateManager.glCreateShader((int)35632);
            int n3 = 0;
            try {
                GlStateManager.glShaderSource((int)n, (String)this.m747(string));
                Pos.setInt19(n);
                GlStateManager.glShaderSource((int)n2, (String)this.m747(string2));
                Pos.setInt19(n2);
                n3 = GlStateManager.glCreateProgram();
                Pos.m875(n3, n, n2);
                this.count115 = n3;
            }
            catch (RuntimeException runtimeException) {
                block8: {
                    int n4;
                    block7: {
                        n4 = n3;
                        if (string4 != null) break block7;
                        if (n4 == 0) break block8;
                        n4 = n3;
                    }
                    GL20C.glDeleteProgram((int)n4);
                }
                throw new IllegalStateException("Failed to create shader program: " + string + " + " + string2, runtimeException);
            }
            finally {
                GlStateManager.glDeleteShader((int)n);
                GlStateManager.glDeleteShader((int)n2);
            }
            if (Module.getTextArray9() != null) break block9;
            Pos.setText12("YMt5jb");
        }
    }

    private String m747(Object object) {
        String string = (String)object;
        String string2 = Pos.getText67();
        try {
            String string3;
            Identifier identifier = Identifier.of((String)"alien", (String)("shaders/" + string));
            String string4 = string3 = new String(((Resource)MinecraftClient.getInstance().getResourceManager().getResource(identifier).orElseThrow()).getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            if (string2 == null) {
                string4 = string4.startsWith("\ufeff") ? string3.substring(1) : string3;
            }
            return string4;
        }
        catch (IOException iOException) {
            throw new IllegalStateException("Shader not found: " + string, iOException);
        }
    }

    public void m1045() {
        GlStateManager._glUseProgram((int)this.count115);
    }

    public String m175(Object object) {
        StringBuilder stringBuilder;
        block2: {
            String[] stringArray = (String[])object;
            StringBuilder stringBuilder2 = new StringBuilder();
            String string = Pos.getText67();
            stringBuilder2.append("program=").append(this.count115).append(",link=").append(GL20C.glGetProgrami((int)this.count115, (int)35714)).append(",info='").append(GL20C.glGetProgramInfoLog((int)this.count115, (int)512).replace('\n', ' ').replace('\r', ' ')).append('\'');
            String string2 = string;
            for (String string3 : stringArray) {
                stringBuilder = stringBuilder2.append(',').append(string3).append('=').append(Pos.m338(this.count115, string3));
                if (string2 == null) {
                    if (string2 == null) continue;
                }
                break block2;
            }
            stringBuilder = stringBuilder2;
        }
        return stringBuilder.toString();
    }

    public void m241(Object object, int n) {
        String string = (String)object;
        int n2 = n;
        GlStateManager._glUniform1i((int)Pos.m338(this.count115, string), (int)n2);
    }

    public void m82(Object object, double d) {
        String string = (String)object;
        double d2 = d;
        GL20C.glUniform1f((int)Pos.m338(this.count115, string), (float)((float)d2));
    }

    public void m700(Object object, double d, double d2) {
        String string = (String)object;
        double d3 = d;
        double d4 = d2;
        GL20C.glUniform2f((int)Pos.m338(this.count115, string), (float)((float)d3), (float)((float)d4));
    }

    public void m77(Object object, double d, double d2, double d3, double d4) {
        String string = (String)object;
        double d5 = d;
        double d6 = d2;
        double d7 = d3;
        double d8 = d4;
        GL20C.glUniform4f((int)Pos.m338(this.count115, string), (float)((float)d5), (float)((float)d6), (float)((float)d7), (float)((float)d8));
    }

    /*
     * Unable to fully structure code
     */
    static {}
}

