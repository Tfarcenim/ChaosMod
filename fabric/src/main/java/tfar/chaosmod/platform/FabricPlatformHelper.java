package tfar.chaosmod.platform;

import org.jetbrains.annotations.Nullable;
import tfar.chaosmod.attachments.CommonDataAttachment;
import tfar.chaosmod.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T> void registerDataAttachment(CommonDataAttachment<T> attachment) {

    }

    @Override
    public <T> @Nullable T getAttachedValue(Object object, CommonDataAttachment<T> attachment) {
        return null;
    }

    @Override
    public <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, @Nullable T value) {

    }
}
