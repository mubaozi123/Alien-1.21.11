/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.Client;
import shit.event.HookTotemParticleInitEvent;
import shit.mixin.ParticleAccessor;

@Environment(value=EnvType.CLIENT)
@Mixin(value={TotemParticle.class})
public abstract class TotemParticleMixin {
    @Inject(method={"<init>(Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/Optional;)V"}, at={@At(value="TAIL")})
    private void m893(ClientWorld clientWorld, double d, double d2, double d3, double d4, double d5, double d6, SpriteProvider spriteProvider, CallbackInfo callbackInfo) {
        ParticleAccessor particleAccessor = (ParticleAccessor)((Object)this);
        HookTotemParticleInitEvent hookTotemParticleInitEvent = new HookTotemParticleInitEvent(particleAccessor.alien$getXd(), particleAccessor.alien$getYd(), particleAccessor.alien$getZd());
        Client.eventBus.m287(hookTotemParticleInitEvent);
        if (!hookTotemParticleInitEvent.isSet85()) {
            return;
        }
        particleAccessor.alien$setXd(hookTotemParticleInitEvent.value129);
        particleAccessor.alien$setYd(hookTotemParticleInitEvent.value200);
        particleAccessor.alien$setZd(hookTotemParticleInitEvent.value123);
        if (hookTotemParticleInitEvent.count184 != 0) {
            ((AnimatedParticle)(Object)this).setColor(hookTotemParticleInitEvent.count184 & 0xFFFFFF);
        }
    }
}

