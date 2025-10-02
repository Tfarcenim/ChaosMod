package tfar.chaosmod.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import tfar.chaosmod.Constants;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.chaosmod.attachments.CommonDataAttachments;

@Mixin(MouseHandler.class)
public class MixinMinecraft {

    @Shadow @Final private Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "turnPlayer",cancellable = true)
    private void init(CallbackInfo info) {
        if (CommonDataAttachments.hasChaosEffect(minecraft.player,"no_camera_movement")) {
            info.cancel();
        }
    }
}