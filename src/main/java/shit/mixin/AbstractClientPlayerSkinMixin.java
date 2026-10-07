/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.module.client.Skin;

@Environment(value=EnvType.CLIENT)
@Mixin(value={AbstractClientPlayerEntity.class})
public abstract class AbstractClientPlayerSkinMixin {
    @Inject(method={"getSkin()Lnet/minecraft/entity/player/SkinTextures;"}, at={@At(value="RETURN")}, cancellable=true)
    private void alien$skin(CallbackInfoReturnable callbackInfoReturnable) {
        Skin skin = Skin.INSTANCE;
        if (skin == null || !skin.isSet19()) {
            return;
        }
        if (MinecraftClient.getInstance().player != (Object) this) {
            return;
        }
        SkinTextures skinTextures = (SkinTextures)callbackInfoReturnable.getReturnValue();
        boolean bl = skin.isSet92() && skin.isSet151();
        callbackInfoReturnable.setReturnValue((Object)new SkinTextures(bl ? skin.getObj9() : skinTextures.body(), skin.isSet162() ? skin.getObj10() : skinTextures.cape(), skin.isSet162() ? skin.getObj10() : skinTextures.elytra(), bl ? skin.getObj21() : skinTextures.model(), skinTextures.secure()));
    }
}

