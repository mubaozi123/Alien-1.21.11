/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.CobwebBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shit.module.movement.FastWeb;

@Environment(value=EnvType.CLIENT)
@Mixin(value={CobwebBlock.class})
public class WebBlockMixin {
    @Inject(method={"onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/EntityCollisionHandler;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void alien$entityInside(BlockState blockState, World world, BlockPos blockPos, Entity entity, EntityCollisionHandler entityCollisionHandler, boolean bl, CallbackInfo callbackInfo) {
        FastWeb fastWeb = FastWeb.INSTANCE;
        if (fastWeb == null || !fastWeb.isSet19()) {
            return;
        }
        if (entity != MinecraftClient.getInstance().player) {
            return;
        }
        if (!fastWeb.isSet116()) {
            return;
        }
        FastWeb.Mode mode = fastWeb.getMode2();
        if (mode == FastWeb.Mode.Ignore) {
            entity.onLanding();
            callbackInfo.cancel();
        } else if (mode == FastWeb.Mode.Custom) {
            double d = fastWeb.getDouble();
            double d2 = fastWeb.getDouble17();
            entity.slowMovement(blockState, new Vec3d(d, d2, d));
            callbackInfo.cancel();
        }
    }
}

