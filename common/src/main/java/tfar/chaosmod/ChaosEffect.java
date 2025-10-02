package tfar.chaosmod;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import tfar.chaosmod.attachments.CommonDataAttachments;
import tfar.chaosmod.platform.Services;

import java.util.ArrayList;
import java.util.List;

public class ChaosEffect {

    public static final List<String> chaosEffects = new ArrayList<>();

    static {
        //chaosEffects.add("no_movement");
       // chaosEffects.add("no_camera_movement");
        chaosEffects.add("no_crouching");
        chaosEffects.add("no_jumping");
        chaosEffects.add("no_item_pickup");
        chaosEffects.add("no_item_drop");
        chaosEffects.add("no_sprinting");

        chaosEffects.add("no_left_click");
        chaosEffects.add("no_right_click");
        chaosEffects.add("no_eating");
        chaosEffects.add("no_crafting");
        chaosEffects.add("no_armor");
    }

    public static int max = 3;

    public static void queueNextEffect(ServerPlayer player) {
        List<String> current = CommonDataAttachments.getActiveChaosEffects(player);
        List<String> newEffects = new ArrayList<>(current);
        List<String> possible = new ArrayList<>(chaosEffects);
        possible.removeIf(current::contains);
        if (!possible.isEmpty()) {
            String string = Util.getRandom(possible, player.getRandom());
            newEffects.add(string);
            player.displayClientMessage(Component.literal(string),true);
            if (newEffects.size() > max) {
                newEffects.remove(player.getRandom().nextInt(max));
            }
            Services.PLATFORM.setAttachedValue(player,CommonDataAttachments.CHAOS_EFFECTS,newEffects);
        }
    }
}
