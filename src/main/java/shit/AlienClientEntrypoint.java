/*
 * Decompiled with CFR 0.152.
 */
package shit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import shit.misc.Auth;

@Environment(value=EnvType.CLIENT)
public final class AlienClientEntrypoint
implements ClientModInitializer {
    public void onInitializeClient() {
        new Auth().onInitializeClient();
    }
}

