package tfar.chaosmod.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tfar.chaosmod.attachments.CommonDataAttachments;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {
    @Redirect(method = "slotChangedCraftingGrid",at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"))
    private static boolean noCrafting(Optional instance, AbstractContainerMenu menu,
                                      Level level,
                                      Player player) {
        if (CommonDataAttachments.hasChaosEffect(player,"no_crafting")) {
            return false;
        }
        return instance.isPresent();
    }
}
