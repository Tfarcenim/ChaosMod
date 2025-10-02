package tfar.chaosmod;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.Input;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.chaosmod.attachments.CommonDataAttachments;
import tfar.chaosmod.platform.Services;

import java.util.ArrayList;
import java.util.List;

@Mod(Constants.MOD_ID)
public class ChaosModNeoforge {

    public ChaosModNeoforge(IEventBus eventBus, Dist dist) {
        NeoForge.EVENT_BUS.addListener(this::playerTick);
        NeoForge.EVENT_BUS.addListener(this::sleep);
        NeoForge.EVENT_BUS.addListener(this::itemPickup);
        NeoForge.EVENT_BUS.addListener(this::rightClickItem);
        NeoForge.EVENT_BUS.addListener(this::rightClickBlock);
        NeoForge.EVENT_BUS.addListener(this::breakBlock);
        NeoForge.EVENT_BUS.addListener(this::attack);
        NeoForge.EVENT_BUS.addListener(this::useItem);
        eventBus.addListener(this::register);
        if (dist.isClient()) {
            Client.init(eventBus);
        }
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        ChaosMod.init();

    }

    void useItem(LivingEntityUseItemEvent.Start event) {
        LivingEntity livingEntity = event.getEntity();
        ItemStack stack = event.getItem();
        if (CommonDataAttachments.hasChaosEffect(livingEntity,"no_eating") && stack.getUseAnimation() == UseAnim.EAT) {
            event.setCanceled(true);
        }
    }

    void sleep(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        Services.PLATFORM.setAttachedValue(player,CommonDataAttachments.CHAOS_EFFECTS,new ArrayList<>());
    }

    void itemPickup(ItemEntityPickupEvent.Pre event) {
        Player player = event.getPlayer();
        if (CommonDataAttachments.hasChaosEffect(player,"no_item_pickup")) {
            event.setCanPickup(TriState.FALSE);
        }
    }

    void register(RegisterEvent event){
        CommonDataAttachments.init();
    }

    void playerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            boolean countdown = CommonDataAttachments.countdownTimer(serverPlayer);
            if (countdown) {
                ChaosEffect.queueNextEffect(serverPlayer);
                if (Services.PLATFORM.isDevelopmentEnvironment()) {
                    player.displayClientMessage(Component.literal(CommonDataAttachments.getActiveChaosEffects(player).toString()),true);
                }
            }
        }
    }

    void breakBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (CommonDataAttachments.hasChaosEffect(player,"no_left_click")) {
            event.setCanceled(true);
        }
    }

    void attack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (CommonDataAttachments.hasChaosEffect(player,"no_left_click")) {
            event.setCanceled(true);
        }
    }


    void rightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (CommonDataAttachments.hasChaosEffect(player,"no_right_click")) {
            event.setCanceled(true);
        }
    }

    void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (CommonDataAttachments.hasChaosEffect(player,"no_right_click")) {
            event.setCanceled(true);
        }
    }

    static class Client{

        public static final LayeredDraw.Layer LAYER = (guiGraphics, deltaTracker) -> {
            Entity entity = Minecraft.getInstance().cameraEntity;
            if (entity instanceof Player player) {
                List<String> effects = CommonDataAttachments.getActiveChaosEffects(player);
                for (int i= 0;i < effects.size();i++) {
                    String s = effects.get(i);
                    guiGraphics.drawString(Minecraft.getInstance().font,Component.literal(s),0,i * 12,0xffffffff,true);
                }
                guiGraphics.drawString(Minecraft.getInstance().font,Component.literal(
                        "Time:" +Services.PLATFORM.getOrCreateAttachedValue(player,CommonDataAttachments.TIMER)/20),0,4 * 12,0xffffffff,true);

            }
        };

        static void init(IEventBus bus) {
            bus.addListener(Client::registerOverlay);
            NeoForge.EVENT_BUS.addListener(Client::movement);
        }

        static void registerOverlay(RegisterGuiLayersEvent event) {
            event.registerAboveAll(Constants.id("overlay"),LAYER);
        }

        static void movement(MovementInputUpdateEvent event) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                Input input = event.getInput();
                if (CommonDataAttachments.hasChaosEffect(player,"no_movement")) {
                    input.jumping = false;
                    input.shiftKeyDown = false;
                    input.forwardImpulse = 0;
                    input.leftImpulse = 0;
                }

                if (CommonDataAttachments.hasChaosEffect(player,"no_jumping")) {
                    input.jumping = false;
                }

                if (CommonDataAttachments.hasChaosEffect(player,"no_crouching")) {
                    input.shiftKeyDown = false;
                }
            }
        }
    }
}

//Not sure if its possible, but you would lose an ability every 10-30 seconds.
//
//Ex. Cant Craft, Cant Break, Cant Place, Cant Deal Damage, Cant Eat, Cant Sleep, etc.

//
//Abilities that can be lost
//Every Minute I Lose an Ability, Abilities Reset Every Time I Sleep
//
//
//Core Game
//
//Movement (walk, sprint, crouch, jump, swim, crawl, climb.)
//
//Camera
//
//Pick up items (walk over dropped items, vacuum into inventory)
//
//Drop items
//
//
//Block Interaction
//
//Breaking (Right Click)
//
//Placing (Left Click)
//
//Crafting (inventory crafting, crafting table)
//
//
//Combat & Damage
//
//Dealing any damage
//
//Blocking/Shielding
//
//Taking damage (from fall, lava, drowning, suffocation, mobs, projectiles, explosions, hunger, etc.)
//
//
//Survival Needs
//
//Eating/Drinking (consumables)
//
//Sleeping
//
//
//Inventory
//
//Wearing armor
//
//Off Hand