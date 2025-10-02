package tfar.chaosmod.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.chaosmod.attachments.CommonDataAttachments;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "drop(Z)Z",at = @At("HEAD"),cancellable = true)
    private void preventDrop(boolean dropStack, CallbackInfoReturnable<Boolean> cir) {
        if (CommonDataAttachments.hasChaosEffect((ServerPlayer)(Object)this,"no_item_drop")) {
            cir.setReturnValue(false);
        }
    }
}
