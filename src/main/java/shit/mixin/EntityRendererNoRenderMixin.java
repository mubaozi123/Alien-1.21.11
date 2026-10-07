/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shit.module.Module;
import shit.module.combat.CrystalChams;
import shit.module.misc.KillEffect;
import shit.module.render.NameTags;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={EntityRenderer.class})
public class EntityRendererNoRenderMixin {
    @Inject(method={"updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V"}, at={@At(value="TAIL")})
    private void alien$extract(Entity entity, EntityRenderState entityRenderState, float f, CallbackInfo callbackInfo) {
        NoRender noRender;
        if (NameTags.m254(entity)) {
            entityRenderState.displayName = null;
        }
        if ((noRender = NoRender.INSTANCE) == null || !noRender.isSet19()) {
            return;
        }
        if (((Boolean)noRender.entityFire.getObj()).booleanValue()) {
            entityRenderState.onFire = false;
        }
        if (((Boolean)noRender.invisible.getObj()).booleanValue()) {
            entityRenderState.invisible = false;
        }
    }

    @Inject(method={"hasLabel(Lnet/minecraft/entity/Entity;D)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void m892(Entity entity, double d, CallbackInfoReturnable callbackInfoReturnable) {
        if (NameTags.m254(entity)) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Inject(method={"shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/Frustum;DDD)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$shouldRender(Entity entity, Frustum frustum, double d, double d2, double d3, CallbackInfoReturnable callbackInfoReturnable) {
        boolean bl;
        Object object;
        if (entity instanceof LivingEntity) {
            object = (LivingEntity)entity;
            if (KillEffect.INSTANCE != null && KillEffect.INSTANCE.m210(object)) {
                callbackInfoReturnable.setReturnValue((Object)false);
                return;
            }
        }
        if (entity instanceof EndCrystalEntity && CrystalChams.m7(object = (EndCrystalEntity)entity)) {
            callbackInfoReturnable.setReturnValue((Object)false);
            return;
        }
        object = NoRender.INSTANCE;
        if (object == null || !((Module)object).isSet19()) {
            return;
        }
        boolean bl2 = bl = (Boolean)((NoRender)object).potions.getObj() != false && entity instanceof PotionEntity || (Boolean)((NoRender)object).xP.getObj() != false && entity instanceof ExperienceBottleEntity || (Boolean)((NoRender)object).arrows.getObj() != false && entity instanceof PersistentProjectileEntity || (Boolean)((NoRender)object).eggs.getObj() != false && entity instanceof EggEntity || (Boolean)((NoRender)object).items.getObj() != false && entity instanceof ItemEntity;
        if (bl) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }
}

