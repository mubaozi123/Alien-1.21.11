/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import shit.module.chat.ExtraChatHistory;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ChatHud.class})
public class ExtraChatHistoryMixin {
    @ModifyConstant(method={"addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V"}, constant={@Constant(intValue=100)})
    private int alien$maxMessages(int n) {
        return ExtraChatHistory.INSTANCE != null ? ExtraChatHistory.INSTANCE.getInt33() : n;
    }
}

