/*
 * Decompiled with CFR 0.152.
 */
package shit;

import net.fabricmc.api.ModInitializer;
import shit.misc.Logger;

public final class AlienEntrypoint
implements ModInitializer {
    private static String text3367;

    public void onInitialize() {
        new Logger().onInitialize();
    }

    public static void setText11(String string) {
        text3367 = string;
    }

    public static String getText52() {
        return text3367;
    }

    static {
        AlienEntrypoint.setText11("xFFDX");
    }
}

