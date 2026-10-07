/*
 * Decompiled with CFR 0.152.
 */
package shit.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import shit.module.render.ArmorHide;
import shit.module.render.NoRender;

@Environment(value=EnvType.CLIENT)
@Mixin(value={EquipmentRenderer.class})
public class EquipmentLayerRendererMixin {
    @Redirect(method={"render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;hasGlint()Z"))
    private boolean alien$noArmorGlint(ItemStack itemStack) {
        if (ArmorHide.isSet136() || NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.armorGlint.getObj()).booleanValue()) {
            return false;
        }
        return itemStack.hasGlint();
    }

    @Redirect(method={"render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"))
    private Object alien$noArmorTrim(ItemStack itemStack, ComponentType componentType) {
        if (componentType == DataComponentTypes.TRIM && (ArmorHide.isSet158() || NoRender.INSTANCE != null && NoRender.INSTANCE.isSet19() && ((Boolean)NoRender.INSTANCE.armorTrim.getObj()).booleanValue())) {
            return null;
        }
        return itemStack.get(componentType);
    }
}

