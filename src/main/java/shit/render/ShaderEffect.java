/*
 * Decompiled with CFR 0.152.
 */
package shit.render;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.awt.Color;
import java.lang.invoke.LambdaMetafactory;
import java.nio.ByteBuffer;
import java.util.OptionalInt;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.DynamicUniformStorage;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import shit.module.Module;
import shit.util.BufferUtil2;
import shit.util.RenderUtil4;

@Environment(value=EnvType.CLIENT)
public class ShaderEffect {
    public static final ShaderEffect shaderEffect = new ShaderEffect();
    private static final int count93 = 0;
    private static final int count84 = 0;
    private RenderPipeline renderPipeline6;
    private RenderPipeline renderPipeline7;
    private RenderPipeline renderPipeline8;
    private RenderPipeline renderPipeline2;
    private RenderPipeline renderPipeline21;
    private RenderPipeline renderPipeline14;
    private Framebuffer field63;
    private Framebuffer field46;
    private Framebuffer field64;
    private final OutlineVertexConsumerProvider field52 = new OutlineVertexConsumerProvider();
    private boolean flag111;
    private boolean flag35;
    private boolean flag52;
    private boolean flag54;
    private boolean flag66;
    private static Module[] modules3;

    private ShaderEffect() {
    }

    public void m987(Object object, Object object2, Object object3, Object object4) {
        Data4 data4;
        Data data;
        Type type;
        Framebuffer framebuffer;
        block6: {
            block3: {
                Framebuffer framebuffer2;
                block5: {
                    int n;
                    block4: {
                        Framebuffer framebuffer3;
                        Module[] moduleArray;
                        block2: {
                            framebuffer = (Framebuffer)object;
                            type = (Type)((Object)object2);
                            data = (Data)object3;
                            data4 = (Data4)object4;
                            moduleArray = ShaderEffect.getModuleArray4();
                            framebuffer3 = framebuffer;
                            if (moduleArray == null) break block2;
                            if (framebuffer3 == null) break block3;
                            framebuffer3 = framebuffer;
                        }
                        n = framebuffer3.textureWidth;
                        if (moduleArray == null) break block4;
                        if (n <= 0) break block3;
                        framebuffer2 = framebuffer;
                        if (moduleArray == null) break block5;
                        n = framebuffer2.textureHeight;
                    }
                    if (n <= 0) break block3;
                    framebuffer2 = framebuffer;
                }
                if (framebuffer2.getColorAttachmentView() != null) break block6;
            }
            return;
        }
        this.m831();
        this.m796(framebuffer.textureWidth, framebuffer.textureHeight);
        Data3 data3 = this.m940(framebuffer.textureWidth, framebuffer.textureHeight, data, data4);
        this.renderPass("alien_shader_effect", framebuffer, this.field63, this.m973((Object)type), data3, this.m594((Object)type));
        Object[] objectArray = new Object[6];
        objectArray[5] = false;
        objectArray[4] = null;
        objectArray[3] = this.renderPipeline14;
        objectArray[2] = framebuffer;
        objectArray[1] = this.field63;
        objectArray[0] = "alien_shader_copy";
        Object[] objectArray2 = objectArray;
        this.renderPass(objectArray2[0], objectArray2[1], objectArray2[2], objectArray2[3], objectArray2[4], (Boolean)objectArray2[5]);
    }

    public void setObj6(Object object) {
        Framebuffer framebuffer;
        block5: {
            block3: {
                int n;
                block4: {
                    Framebuffer framebuffer2;
                    Module[] moduleArray;
                    block2: {
                        framebuffer = (Framebuffer)object;
                        moduleArray = ShaderEffect.getModuleArray4();
                        framebuffer2 = framebuffer;
                        if (moduleArray == null) break block2;
                        if (framebuffer2 == null) break block3;
                        framebuffer2 = framebuffer;
                    }
                    n = framebuffer2.textureWidth;
                    if (moduleArray == null) break block4;
                    if (n <= 0) break block3;
                    n = framebuffer.textureHeight;
                }
                if (n > 0) break block5;
            }
            return;
        }
        this.m802(framebuffer.textureWidth, framebuffer.textureHeight);
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
        commandEncoder.clearColorAndDepthTextures(this.field64.getColorAttachment(), 0, this.field64.getDepthAttachment(), 1.0);
    }

    private void renderPass(Object var1_1, Object var2_2, Object var3_3, Object var4_4, Object var5_5, boolean var6_6) {
        String string = (String)var1_1;
        Framebuffer framebuffer = (Framebuffer)var2_2;
        Framebuffer framebuffer2 = (Framebuffer)var3_3;
        RenderPipeline renderPipeline = (RenderPipeline)var4_4;
        Data3 data3 = (Data3)var5_5;
        boolean bl = var6_6;
        if (framebuffer.getColorAttachmentView() == null) {
            return;
        }
        if (framebuffer2.getColorAttachmentView() == null) {
            return;
        }
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
        net.minecraft.client.gl.GpuSampler gpuSampler = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
        try (RenderPass renderPass = commandEncoder.createRenderPass(() -> string, framebuffer2.getColorAttachmentView(), OptionalInt.empty())) {
            renderPass.setPipeline(renderPipeline);
            RenderSystem.bindDefaultUniforms(renderPass);
            if (data3 != null) {
                renderPass.setUniform("ShaderParams", data3.getGpuBufferSlice());
                if (bl) {
                    renderPass.setUniform("ShaderColors", data3.gpuBufferSlice2());
                }
            }
            renderPass.bindTexture("InputSampler", framebuffer.getColorAttachmentView(), gpuSampler);
            renderPass.draw(0, 3);
        }
    }

    private Data3 m940(int n, int n2, Object object, Object object2) {
        int n3 = n;
        int n4 = n2;
        Data data = (Data)object;
        Data4 data4 = (Data4)object2;
        float f = Math.max(1.0f, (float)n3);
        float f2 = Math.max(1.0f, (float)n4);
        float f3 = Math.max(1.0f, RenderUtil4.getFloat57());
        float f4 = Math.max(1.0f, RenderUtil4.getFloat53());
        Color color = data4.color6();
        Color color2 = data4.color7();
        Color color3 = data4.color8();
        Color color4 = data4.color9();
        Color color5 = data4.getColor4();
        Color color6 = data4.getColor5();
        return new Data3(RenderUtil4.m1027("shader_params", "Alien Shader Params UBO", count93, 8, new Vec13f(f, f2, data.value99(), data.value100(), data.isSet10() ? -1.0f : ShaderEffect.m6(color), data.getFloat21() / 255.0f, data.value102() / 255.0f, (float)(Util.getMeasuringTimeMs() % 100000L) / 1000.0f, data.getFloat(), data.getFloat37(), data.value105(), f3, f4)), RenderUtil4.m1027("shader_colors", "Alien Shader Colors UBO", count84, 8, new Data2(color, color2, color3, color4, color5, color6)));
    }

    private void m831() {
        block3: {
            block2: {
                Module[] moduleArray = ShaderEffect.getModuleArray4();
                ShaderEffect shaderEffect = this;
                if (moduleArray == null) break block2;
                if (shaderEffect.renderPipeline6 != null) break block3;
                this.renderPipeline6 = this.m628("outline", true);
                this.renderPipeline7 = this.m628("smoke", true);
                this.renderPipeline8 = this.m628("gradient", false);
                this.renderPipeline2 = this.m628("snow", true);
                this.renderPipeline21 = this.m628("fade", true);
                shaderEffect = this;
            }
            shaderEffect.renderPipeline14 = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.POST_EFFECT_PROCESSOR_SNIPPET}).withLocation(BufferUtil2.m52("pipelines/shader_copy")).withVertexShader("core/screenquad").withFragmentShader(BufferUtil2.m52("shader_copy")).withSampler("InputSampler").withCull(false).build();
        }
    }

    private void m796(int n, int n2) {
        block7: {
            Framebuffer framebuffer;
            int n3;
            int n4;
            block5: {
                block6: {
                    n4 = n;
                    n3 = n2;
                    Module[] moduleArray = ShaderEffect.getModuleArray4();
                    framebuffer = this.field63;
                    if (moduleArray != null) {
                        if (framebuffer == null) {
                            this.field63 = new SimpleFramebuffer("Alien Shader Swap", n4, n3, false);
                        }
                        framebuffer = this.field63;
                    }
                    if (moduleArray == null) break block5;
                    if (framebuffer.textureWidth != n4) break block6;
                    framebuffer = this.field63;
                    if (moduleArray == null) break block5;
                    if (framebuffer.textureHeight == n3) break block7;
                }
                framebuffer = this.field63;
            }
            framebuffer.resize(n4, n3);
        }
    }

    private void m174(int n, int n2) {
        block7: {
            Framebuffer framebuffer;
            int n3;
            int n4;
            block5: {
                block6: {
                    n4 = n;
                    n3 = n2;
                    Module[] moduleArray = ShaderEffect.getModuleArray4();
                    framebuffer = this.field46;
                    if (moduleArray != null) {
                        if (framebuffer == null) {
                            this.field46 = new SimpleFramebuffer("Alien Shader Hands", n4, n3, true);
                        }
                        framebuffer = this.field46;
                    }
                    if (moduleArray == null) break block5;
                    if (framebuffer.textureWidth != n4) break block6;
                    framebuffer = this.field46;
                    if (moduleArray == null) break block5;
                    if (framebuffer.textureHeight == n3) break block7;
                }
                framebuffer = this.field46;
            }
            framebuffer.resize(n4, n3);
        }
    }

    private void m802(int n, int n2) {
        block7: {
            Framebuffer framebuffer;
            int n3;
            int n4;
            block5: {
                block6: {
                    n4 = n;
                    n3 = n2;
                    Module[] moduleArray = ShaderEffect.getModuleArray4();
                    framebuffer = this.field64;
                    if (moduleArray != null) {
                        if (framebuffer == null) {
                            this.field64 = new SimpleFramebuffer("Alien Shader Chests", n4, n3, true);
                        }
                        framebuffer = this.field64;
                    }
                    if (moduleArray == null) break block5;
                    if (framebuffer.textureWidth != n4) break block6;
                    framebuffer = this.field64;
                    if (moduleArray == null) break block5;
                    if (framebuffer.textureHeight == n3) break block7;
                }
                framebuffer = this.field64;
            }
            framebuffer.resize(n4, n3);
        }
    }

    private RenderPipeline m628(Object object, boolean bl) {
        String string = (String)object;
        boolean bl2 = bl;
        RenderPipeline.Builder builder = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{RenderPipelines.POST_EFFECT_PROCESSOR_SNIPPET}).withLocation(BufferUtil2.m52("pipelines/shader_" + string)).withVertexShader(Identifier.ofVanilla((String)"core/screenquad")).withFragmentShader(BufferUtil2.m52("shader_" + string)).withCull(false);
        Module[] moduleArray = ShaderEffect.getModuleArray4();
        RenderPipeline.Builder builder2 = builder.withUniform("ShaderParams", UniformType.UNIFORM_BUFFER);
        if (moduleArray != null) {
            if (bl2) {
                builder.withUniform("ShaderColors", UniformType.UNIFORM_BUFFER);
            }
            builder2 = builder.withSampler("InputSampler");
        }
        return builder2.build();
    }

    private RenderPipeline m973(Object object) {
        Type type = (Type)((Object)object);
        return switch (type.ordinal()) {
            case 1 -> this.renderPipeline7;
            case 2 -> this.renderPipeline8;
            case 3 -> this.renderPipeline2;
            case 4 -> this.renderPipeline21;
            default -> this.renderPipeline6;
        };
    }

    private boolean m594(Object object) {
        Type type = (Type)((Object)object);
        return type != Type.Gradient;
    }

    private static float m693(Object object) {
        Color color = (Color)object;
        return (float)color.getRed() / 255.0f;
    }

    private static float m95(Object object) {
        Color color = (Color)object;
        return (float)color.getGreen() / 255.0f;
    }

    private static float m939(Object object) {
        Color color = (Color)object;
        return (float)color.getBlue() / 255.0f;
    }

    private static float m6(Object object) {
        Color color = (Color)object;
        return (float)color.getAlpha() / 255.0f;
    }

    private static /* synthetic */ String cfrlam$renderPass$0(String string) {
        return string;
    }

    /*
     * Unable to fully structure code
     */
    static {}

    public static void setModuleArray2(Module[] moduleArray) {
        modules3 = moduleArray;
    }

    public static Module[] getModuleArray4() {
        return modules3;
    }

    @Environment(value=EnvType.CLIENT)
    static final class Data3  {
        private final GpuBufferSlice gpuBufferSlice;
        private final GpuBufferSlice gpuBufferSlice2;

        private Data3(GpuBufferSlice gpuBufferSlice, GpuBufferSlice gpuBufferSlice2) {
            this.gpuBufferSlice = gpuBufferSlice;
            this.gpuBufferSlice2 = gpuBufferSlice2;
        }

        public GpuBufferSlice getGpuBufferSlice() {
            return this.gpuBufferSlice;
        }

        public GpuBufferSlice gpuBufferSlice2() {
            return this.gpuBufferSlice2;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static final class Data4  {
        private final Color color6;
        private final Color color7;
        private final Color color8;
        private final Color color9;
        private final Color color10;
        private final Color color11;

        public Data4(Color color, Color color2, Color color3, Color color4, Color color5, Color color6) {
            this.color6 = color;
            this.color7 = color2;
            this.color8 = color3;
            this.color9 = color4;
            this.color10 = color5;
            this.color11 = color6;
        }

        public Color color6() {
            return this.color6;
        }

        public Color color7() {
            return this.color7;
        }

        public Color color8() {
            return this.color8;
        }

        public Color color9() {
            return this.color9;
        }

        public Color getColor4() {
            return this.color10;
        }

        public Color getColor5() {
            return this.color11;
        }
    }

    @Environment(value=EnvType.CLIENT)
    static final class Data2 
    implements DynamicUniformStorage.Uploadable {
        private final Color color14;
        private final Color color15;
        private final Color color16;
        private final Color color17;
        private final Color color18;
        private final Color color19;

        private Data2(Color color, Color color2, Color color3, Color color4, Color color5, Color color6) {
            this.color14 = color;
            this.color15 = color2;
            this.color16 = color3;
            this.color17 = color4;
            this.color18 = color5;
            this.color19 = color6;
        }

        public void write(ByteBuffer byteBuffer) {
            block0: {
                Module[] moduleArray = ShaderEffect.getModuleArray4();
                Std140Builder.intoBuffer((ByteBuffer)byteBuffer).putVec4(ShaderEffect.m693(this.color14), ShaderEffect.m95(this.color14), ShaderEffect.m939(this.color14), ShaderEffect.m6(this.color14)).putVec4(ShaderEffect.m693(this.color15), ShaderEffect.m95(this.color15), ShaderEffect.m939(this.color15), ShaderEffect.m6(this.color15)).putVec4(ShaderEffect.m693(this.color16), ShaderEffect.m95(this.color16), ShaderEffect.m939(this.color16), ShaderEffect.m6(this.color16)).putVec4(ShaderEffect.m693(this.color17), ShaderEffect.m95(this.color17), ShaderEffect.m939(this.color17), ShaderEffect.m6(this.color17)).putVec4(ShaderEffect.m693(this.color18), ShaderEffect.m95(this.color18), ShaderEffect.m939(this.color18), ShaderEffect.m6(this.color18)).putVec4(ShaderEffect.m693(this.color19), ShaderEffect.m95(this.color19), ShaderEffect.m939(this.color19), ShaderEffect.m6(this.color19));
                Module[] moduleArray2 = moduleArray;
                if (moduleArray2 != null) break block0;
                Module.setTextArray9(new String[1]);
            }
        }

        public Color getColor8() {
            return this.color14;
        }

        public Color color15() {
            return this.color15;
        }

        public Color getColor2() {
            return this.color16;
        }

        public Color color17() {
            return this.color17;
        }

        public Color getColor6() {
            return this.color18;
        }

        public Color color19() {
            return this.color19;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Type {
      Default, Smoke, Gradient, Snow, Fade;

      private Type() {}



        private static Type[] getTypeArray2() {
            return new Type[]{Default, Smoke, Gradient, Snow, Fade};
        }
    
   }

    @Environment(value=EnvType.CLIENT)
    static final class Vec13f 
    implements DynamicUniformStorage.Uploadable {
        private final float value86;
        private final float value87;
        private final float value88;
        private final float value89;
        private final float value90;
        private final float value91;
        private final float value92;
        private final float value93;
        private final float value94;
        private final float value95;
        private final float value96;
        private final float value97;
        private final float value98;

        private Vec13f(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
            this.value86 = f;
            this.value87 = f2;
            this.value88 = f3;
            this.value89 = f4;
            this.value90 = f5;
            this.value91 = f6;
            this.value92 = f7;
            this.value93 = f8;
            this.value94 = f9;
            this.value95 = f10;
            this.value96 = f11;
            this.value97 = f12;
            this.value98 = f13;
        }

        public void write(ByteBuffer byteBuffer) {
            block0: {
                ShaderEffect.getModuleArray4();
                Std140Builder.intoBuffer((ByteBuffer)byteBuffer).putVec2(this.value86, this.value87).putVec2(1.0f / this.value86, 1.0f / this.value87).putFloat(this.value88).putFloat(this.value89).putFloat(this.value90).putFloat(this.value91).putFloat(this.value92).putFloat(this.value93).putFloat(this.value94).putFloat(this.value95).putFloat(this.value96).putVec2(this.value97, this.value98);
                if (Module.getTextArray9() != null) break block0;
                ShaderEffect.setModuleArray2(new Module[3]);
            }
        }

        public float getFloat32() {
            return this.value86;
        }

        public float value87() {
            return this.value87;
        }

        public float getFloat8() {
            return this.value88;
        }

        public float value89() {
            return this.value89;
        }

        public float value90() {
            return this.value90;
        }

        public float value91() {
            return this.value91;
        }

        public float getFloat2() {
            return this.value92;
        }

        public float value93() {
            return this.value93;
        }

        public float value94() {
            return this.value94;
        }

        public float value95() {
            return this.value95;
        }

        public float value96() {
            return this.value96;
        }

        public float value97() {
            return this.value97;
        }

        public float value98() {
            return this.value98;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static final class Data  {
        private final float value99;
        private final float value100;
        private final boolean flag10;
        private final float value101;
        private final float value102;
        private final float value103;
        private final float value104;
        private final float value105;

        public Data(float f, float f2, boolean bl, float f3, float f4, float f5, float f6, float f7) {
            this.value99 = f;
            this.value100 = f2;
            this.flag10 = bl;
            this.value101 = f3;
            this.value102 = f4;
            this.value103 = f5;
            this.value104 = f6;
            this.value105 = f7;
        }

        public float value99() {
            return this.value99;
        }

        public float value100() {
            return this.value100;
        }

        public boolean isSet10() {
            return this.flag10;
        }

        public float getFloat21() {
            return this.value101;
        }

        public float value102() {
            return this.value102;
        }

        public float getFloat() {
            return this.value103;
        }

        public float getFloat37() {
            return this.value104;
        }

        public float value105() {
            return this.value105;
        }
    }
}

