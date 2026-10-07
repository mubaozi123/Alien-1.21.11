/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import shit.module.chat.PortalChat;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ClientPlayerEntity.class})
public class PortalChatMixin {
    @Redirect(method={"tickNausea(Z)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void alien$portalChat(MinecraftClient minecraftClient, Screen screen) {
        if (screen == null && PortalChat.INSTANCE != null && PortalChat.INSTANCE.isSet19()) {
            return;
        }
        minecraftClient.setScreen(screen);
    }
}

