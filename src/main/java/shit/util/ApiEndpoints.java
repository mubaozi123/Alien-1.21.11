/*
 * Decompiled with CFR 0.152.
 */
package shit.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
final class ApiEndpoints {
    private ApiEndpoints() {
    }

    static String getText65() {
        return "http://neko.antichest.pw/api/index.php?route=/verify";
    }

    static String m39(Object object) {
        String string = (String)object;
        return "https://download.neko.antichest.pw/api/alien/index.php?route=" + string;
    }
}

