/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.Client;
import shit.module.chat.ChatSuffix;
import shit.module.chat.Emoji;
import shit.module.chat.FancyChat;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ChatScreen.class})
public class ChatScreenMixin {
    @Inject(method={"sendMessage(Ljava/lang/String;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$handleChatInput(String string, boolean bl, CallbackInfo callbackInfo) {
        if (Client.commandManager.m571(string)) {
            callbackInfo.cancel();
        }
    }

    @ModifyArg(method={"sendMessage(Ljava/lang/String;Z)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendChatMessage(Ljava/lang/String;)V"), index=0)
    private String m887(String string) {
        ChatSuffix chatSuffix;
        FancyChat fancyChat;
        Emoji emoji = Emoji.INSTANCE;
        if (emoji != null && emoji.isSet19()) {
            string = emoji.m528(string);
        }
        if ((fancyChat = FancyChat.INSTANCE) != null && fancyChat.isSet19()) {
            string = fancyChat.m342(string);
        }
        if ((chatSuffix = ChatSuffix.INSTANCE) != null && chatSuffix.isSet19()) {
            return chatSuffix.m778(string);
        }
        return string;
    }
}

