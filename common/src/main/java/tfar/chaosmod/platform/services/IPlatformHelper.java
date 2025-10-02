package tfar.chaosmod.platform.services;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import tfar.chaosmod.attachments.CommonDataAttachment;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <T> void registerDataAttachment(CommonDataAttachment<T> attachment);
    @Nullable
    <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment);
    default <T> T getOrCreateAttachedValue(Entity entity, CommonDataAttachment<T> attachment) {
        T value = getAttachedValue(entity,attachment);
        if (value!=null) {
            return value;
        }
        setAttachedValue(entity,attachment,attachment.getDefaultValueSupplier().apply(entity));
        T newValue = getAttachedValue(entity,attachment);
        return newValue;
    }
    <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment,@Nullable T value);
}