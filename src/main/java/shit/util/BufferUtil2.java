/*
 * Decompiled with CFR 0.152.
 */
package shit.util;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MemoryUtil;
import shit.util.MC;

@Environment(value=EnvType.CLIENT)
public final class BufferUtil2 {
    private BufferUtil2() {
    }

    public static Identifier m52(Object object) {
        String string = (String)object;
        return Identifier.of((String)"alien", (String)string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ByteBuffer m37(Object object) {
        Identifier identifier = (Identifier)object;
        ResourceManager resourceManager = MC.client3.getResourceManager();
        Optional optional = resourceManager.getResource(identifier);
        if (optional.isEmpty()) {
            throw new RuntimeException("Couldn't find resource at " + String.valueOf(identifier));
        }
        try {
            ByteBuffer byteBuffer = null;
            InputStream inputStream = ((Resource)optional.get()).getInputStream();
            try {
                byte[] byArray = inputStream.readAllBytes();
                ByteBuffer byteBuffer2 = MemoryUtil.memAlloc((int)byArray.length);
                byteBuffer2.put(byArray);
                byteBuffer2.flip();
                byteBuffer = byteBuffer2;
                return byteBuffer;
            }
            finally {
                if (inputStream == null) return byteBuffer;
                inputStream.close();
            }
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to read file", exception);
        }
    }
}

