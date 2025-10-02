package tfar.chaosmod.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.chaosmod.attachments.CommonDataAttachments;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(method = "canStartSprinting",at = @At("RETURN"),cancellable = true)
    private void blockSprint(CallbackInfoReturnable<Boolean> cir) {
        if (CommonDataAttachments.hasChaosEffect((LocalPlayer)(Object)this,"no_sprinting")) {
            cir.setReturnValue(false);
        }
    }
}
