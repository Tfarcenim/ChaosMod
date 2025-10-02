package tfar.chaosmod.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.chaosmod.attachments.CommonDataAttachments;

@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {
    @Inject(method = "canEquip",at = @At("RETURN"),cancellable = true)
    private void preventEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (CommonDataAttachments.hasChaosEffect(entity,"no_armor")) {
            cir.setReturnValue(false);
        }
    }
}
