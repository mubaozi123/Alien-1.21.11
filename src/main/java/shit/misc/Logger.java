/*
 * Decompiled with CFR 0.152.
 */
package shit.misc;

import net.fabricmc.api.ModInitializer;
import org.slf4j.LoggerFactory;

public class Logger
implements ModInitializer {
    public static final org.slf4j.Logger logger2 = org.slf4j.LoggerFactory.getLogger("Alien");
    private static boolean flag136;

    public void onInitialize() {
        logger2.info("{} common initialized.", (Object)"Alien");
    }

    /*
     * Unable to fully structure code
     */
    static {}

    public static void setFlag(boolean bl) {
        flag136 = bl;
    }

    public static boolean isSet62() {
        return flag136;
    }

    public static boolean isSet53() {
        boolean bl = true;
        return !true;
    }
}

