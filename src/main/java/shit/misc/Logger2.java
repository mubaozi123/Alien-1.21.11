/*
 * Decompiled with CFR 0.152.
 */
package shit.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(value=EnvType.CLIENT)
public class Logger2 {
    private static final Logger logger = LoggerFactory.getLogger("Alien");
    private static final MinecraftClient client4 = MinecraftClient.getInstance();

    private String m943(Object object, Object object2) {
        String string = (String)object;
        Object[] objectArray = (Object[])object2;
        StringBuilder stringBuilder = new StringBuilder();
        Object var6_6 = null;
        int n = 0;
        int n2 = 0;
        while (n2 < string.length()) {
            block3: {
                block5: {
                    block4: {
                        if (n2 + 1 >= string.length()) break block3;
                        if (string.charAt(n2) != '{') break block3;
                        if (string.charAt(n2 + 1) != '}') break block3;
                        if (n >= objectArray.length) break block4;
                        stringBuilder.append(objectArray[n++]);
                        if (null == null) break block5;
                    }
                    stringBuilder.append("{}");
                }
                n2 += 2;
                if (null == null) continue;
            }
            stringBuilder.append(string.charAt(n2));
            ++n2;
            if (null == null) continue;
        }
        return stringBuilder.toString();
    }

    /*
     * Unable to fully structure code
     */
    static {}
}

