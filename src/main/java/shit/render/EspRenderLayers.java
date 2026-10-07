/*
 * Decompiled with CFR 0.152.
 */
package shit.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import shit.module.Module;
import shit.util.MC;
import shit.util.RenderLayers;

@Environment(value=EnvType.CLIENT)
public final class EspRenderLayers
implements MC {
    private static RenderLayer renderLayer2;
    private static RenderLayer renderLayer;
    private static boolean flag176;
    private static boolean flag103;
    private static String text2560;

    private EspRenderLayers() {
    }

    private static VertexConsumerProvider.Immediate getObj2() {
        return MC.client3.getBufferBuilders().getEntityVertexConsumers();
    }

    private static Vec3d getVec3d2() {
        return MC.client3.gameRenderer.getCamera().getCameraPos();
    }

    private static RenderLayer m524(boolean bl) {
        boolean bl2 = bl;
        Object var3_2 = null;
        if (!bl2) {
            return net.minecraft.client.render.RenderLayers.debugFilledBox();
        }
        if (!flag176) {
            flag176 = true;
            try {
                renderLayer2 = EspRenderLayers.m373(net.minecraft.client.render.RenderLayers.debugFilledBox(), "filled_through");
            }
            catch (Throwable throwable) {
                renderLayer2 = null;
            }
        }
        return renderLayer2 != null ? renderLayer2 : net.minecraft.client.render.RenderLayers.debugFilledBox();
    }

    private static RenderLayer m1011(boolean bl) {
        boolean bl2 = bl;
        Object var3_2 = null;
        if (!bl2) {
            return net.minecraft.client.render.RenderLayers.lines();
        }
        if (!flag103) {
            flag103 = true;
            try {
                renderLayer = EspRenderLayers.m373(net.minecraft.client.render.RenderLayers.lines(), "lines_through");
            }
            catch (Throwable throwable) {
                renderLayer = null;
            }
        }
        return renderLayer != null ? renderLayer : net.minecraft.client.render.RenderLayers.lines();
    }

    public static RenderLayer m373(Object object, Object object2) {
        RenderLayer renderLayer;
        block2: {
            RenderLayer renderLayer2 = (RenderLayer)object;
            String string = (String)object2;
            RenderPipeline renderPipeline = renderLayer2.getRenderPipeline();
            RenderPipeline.Builder builder = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[0]);
            builder.withLocation(Identifier.of((String)"alien", (String)("pipeline/" + string)));
            builder.withVertexShader(renderPipeline.getVertexShader());
            builder.withFragmentShader(renderPipeline.getFragmentShader());
            builder.withVertexFormat(renderPipeline.getVertexFormat(), renderPipeline.getVertexFormatMode());
            builder.withCull(renderPipeline.isCull());
            builder.withPolygonMode(renderPipeline.getPolygonMode());
            Object var5_6 = null;
            renderPipeline.getBlendFunction().ifPresentOrElse(arg_0 -> ((RenderPipeline.Builder)builder).withBlend(arg_0), () -> ((RenderPipeline.Builder)builder).withoutBlend());
            builder.withColorWrite(renderPipeline.isWriteColor(), renderPipeline.isWriteAlpha());
            builder.withDepthWrite(false);
            builder.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST);
            builder.withDepthBias(renderPipeline.getDepthBiasScaleFactor(), renderPipeline.getDepthBiasConstant());
            for (Object object3 : renderPipeline.getUniforms()) {
                builder.withUniform(((RenderPipeline.UniformDescription)object3).name(), ((RenderPipeline.UniformDescription)object3).type());
                if (null == null) continue;
            }
            for (Object object3 : renderPipeline.getSamplers()) {
                builder.withSampler((String)object3);
                if (null == null) continue;
            }
            RenderSetup renderSetup = RenderSetup.builder((RenderPipeline)builder.build()).build();
            renderLayer = RenderLayers.m79("alien_" + string, renderSetup);
            if (Module.getTextArray9() != null) break block2;
            EspRenderLayers.setText9("wfG9S");
        }
        return renderLayer;
    }

    public static void m69(Object object, Object object2, int n, boolean bl) {
        block0: {
            Matrix4f matrix4f = (Matrix4f)object;
            Box box = (Box)object2;
            int n2 = n;
            boolean bl2 = bl;
            Vec3d vec3d = EspRenderLayers.getVec3d2();
            VertexConsumer vertexConsumer = EspRenderLayers.getObj2().getBuffer(EspRenderLayers.m524(bl2));
            int n3 = n2 >>> 24 & 0xFF;
            int n4 = n2 >> 16 & 0xFF;
            int n5 = n2 >> 8 & 0xFF;
            int n6 = n2 & 0xFF;
            float f = (float)(box.minX - vec3d.x);
            Object var9_15 = null;
            float f2 = (float)(box.minY - vec3d.y);
            float f3 = (float)(box.minZ - vec3d.z);
            float f4 = (float)(box.maxX - vec3d.x);
            float f5 = (float)(box.maxY - vec3d.y);
            float f6 = (float)(box.maxZ - vec3d.z);
            Matrix4f matrix4f2 = matrix4f;
            EspRenderLayers.m412(vertexConsumer, matrix4f2, f, f2, f3, f, f5, f3, f4, f5, f3, f4, f2, f3, n4, n5, n6, n3);
            EspRenderLayers.m412(vertexConsumer, matrix4f2, f4, f2, f6, f4, f5, f6, f, f5, f6, f, f2, f6, n4, n5, n6, n3);
            EspRenderLayers.m412(vertexConsumer, matrix4f2, f, f2, f6, f, f5, f6, f, f5, f3, f, f2, f3, n4, n5, n6, n3);
            EspRenderLayers.m412(vertexConsumer, matrix4f2, f4, f2, f3, f4, f5, f3, f4, f5, f6, f4, f2, f6, n4, n5, n6, n3);
            EspRenderLayers.m412(vertexConsumer, matrix4f2, f, f2, f6, f, f2, f3, f4, f2, f3, f4, f2, f6, n4, n5, n6, n3);
            EspRenderLayers.m412(vertexConsumer, matrix4f2, f, f5, f3, f, f5, f6, f4, f5, f6, f4, f5, f3, n4, n5, n6, n3);
            if (null == null) break block0;
            Module.setTextArray9(new String[3]);
        }
    }

    public static void m688(Object object, Object object2, int n, boolean bl) {
        Matrix4f matrix4f = (Matrix4f)object;
        Box box = (Box)object2;
        int n2 = n;
        boolean bl2 = bl;
        Vec3d vec3d = EspRenderLayers.getVec3d2();
        VertexConsumer vertexConsumer = EspRenderLayers.getObj2().getBuffer(EspRenderLayers.m1011(bl2));
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix((Matrix4fc)matrix4f);
        VoxelShape voxelShape = VoxelShapes.cuboid((Box)box);
        VertexRendering.drawOutline((MatrixStack)matrixStack, (VertexConsumer)vertexConsumer, (VoxelShape)voxelShape, (double)(-vec3d.x), (double)(-vec3d.y), (double)(-vec3d.z), (int)n2, (float)1.0f);
    }

    public static void m125() {
        EspRenderLayers.getObj2().draw();
    }

    private static void m412(Object object, Object object2, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, int n, int n2, int n3, int n4) {
        VertexConsumer vertexConsumer = (VertexConsumer)object;
        Matrix4f matrix4f = (Matrix4f)object2;
        float f13 = f;
        float f14 = f2;
        float f15 = f3;
        float f16 = f4;
        float f17 = f5;
        float f18 = f6;
        float f19 = f7;
        float f20 = f8;
        float f21 = f9;
        float f22 = f10;
        float f23 = f11;
        float f24 = f12;
        int n5 = n;
        int n6 = n2;
        int n7 = n3;
        int n8 = n4;
        vertexConsumer.vertex((Matrix4fc)matrix4f, f13, f14, f15).color(n5, n6, n7, n8);
        vertexConsumer.vertex((Matrix4fc)matrix4f, f16, f17, f18).color(n5, n6, n7, n8);
        vertexConsumer.vertex((Matrix4fc)matrix4f, f19, f20, f21).color(n5, n6, n7, n8);
        vertexConsumer.vertex((Matrix4fc)matrix4f, f22, f23, f24).color(n5, n6, n7, n8);
    }

    public static void setText9(String string) {
        text2560 = string;
    }

    public static String getText61() {
        return text2560;
    }

    static {
        int n = -1;
        EspRenderLayers.setText9(null);
    }
}

