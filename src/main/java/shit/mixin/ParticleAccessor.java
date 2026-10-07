/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(value=EnvType.CLIENT)
@Mixin(value={Particle.class})
public interface ParticleAccessor {
    @Accessor(value="velocityX")
    public double alien$getXd();

    @Accessor(value="velocityX")
    public void alien$setXd(double var1);

    @Accessor(value="velocityY")
    public double alien$getYd();

    @Accessor(value="velocityY")
    public void alien$setYd(double var1);

    @Accessor(value="velocityZ")
    public double alien$getZd();

    @Accessor(value="velocityZ")
    public void alien$setZd(double var1);
}

