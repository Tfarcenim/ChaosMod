package tfar.chaosmod.attachments;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import tfar.chaosmod.platform.Services;

import java.util.*;

public class CommonDataAttachments {

    private static final Map<ResourceLocation,CommonDataAttachment<?>> MAP =new HashMap<>();

    public static final CommonDataAttachment<List<String>> CHAOS_EFFECTS = register(CommonDataAttachment
            .<List<String>>create(o -> new ArrayList<>()).networkSynchronized(ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8)).build("chaos_effects"));

    public static final CommonDataAttachment<Integer> TIMER = register(CommonDataAttachment.create(o -> 0).networkSynchronized(ByteBufCodecs.INT).build("timer"));

    public static CommonDataAttachment<?> lookup(ResourceLocation location) {
        return MAP.get(location);
    }

    static <T> CommonDataAttachment<T> register(CommonDataAttachment<T> type) {
        Services.PLATFORM.registerDataAttachment(type);
        Objects.requireNonNull(type.getAttachment());
        MAP.put(type.name,type);
        return type;
    }

    public static boolean countdownTimer(ServerPlayer player) {
        int timer = Services.PLATFORM.getOrCreateAttachedValue(player,TIMER);
        timer--;
        if (timer <= 0) {
            timer = 1200;//200 + player.getRandom().nextInt(400);
            Services.PLATFORM.setAttachedValue(player,TIMER,timer);
            return true;
        }
        Services.PLATFORM.setAttachedValue(player,TIMER,timer);
        return false;
    }

    public static List<String> getActiveChaosEffects(LivingEntity player) {
        return Services.PLATFORM.getAttachedValue(player,CHAOS_EFFECTS);
    }

    public static boolean hasChaosEffect(LivingEntity player, String effect) {
        return getActiveChaosEffects(player).contains(effect);
    }

    public static void init() {

    }
}
