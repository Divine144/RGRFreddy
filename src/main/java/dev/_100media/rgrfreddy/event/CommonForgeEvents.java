package dev._100media.rgrfreddy.event;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.init.ItemInit;
import dev._100media.rgrfreddy.quest.goal.*;
import com.mojang.brigadier.Command;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.hundredmediaquests.cap.QuestHolderAttacher;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RGRFreddy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onAdvancementEarn(AdvancementEvent.AdvancementEarnEvent event) {
        String advancementID = event.getAdvancement().getId().toString();
        QuestHolderAttacher.checkAllGoals(event.getEntity(), goal -> {
            if (goal instanceof AquireAdvancementGoal advancementGoal) {
                if (advancementID.contains(advancementGoal.getAdvancementID())) {
                    advancementGoal.addProgress(1);
                    return true;
                }
            }
            return false;
        });
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal(RGRFreddy.MODID)
                .then(Commands.literal("test")
                        .executes(context -> Command.SINGLE_SUCCESS)
                )
        );
    }

    @SubscribeEvent
    public static void onPlayerCraft(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getCrafting().is(Items.MUSIC_DISC_5)) {
                FreddyUtils.addToGenericQuestGoal(player, CraftDiscGoal.class);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {

    }

    @SubscribeEvent
    public static void onVillagerTrade(TradeWithVillagerEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack costStack = event.getMerchantOffer().getCostA();
            if (costStack.is(Items.EMERALD)) {
                FreddyUtils.addToGenericQuestGoal(player, TradeEmeraldsGoal.class, costStack.getCount());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(EntityLeaveLevelEvent event) {

    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {

    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {

    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack stack = event.getStack();
            if (stack.getItem() instanceof TieredItem item && item.getTier() == Tiers.GOLD) {
                if (stack.isEnchanted()) {
                    FreddyUtils.addToGenericQuestGoal(player, AquireGoldenItemGoal.class);
                }
            }
            else if (stack.getItem() instanceof ArmorItem item && item.getMaterial() == ArmorMaterials.GOLD) {
                if (stack.isEnchanted()) {
                    FreddyUtils.addToGenericQuestGoal(player, AquireGoldenItemGoal.class);
                }
            }
            if (stack.is(ItemTags.MUSIC_DISCS)) {
                FreddyUtils.addToGenericQuestGoal(player, AquireMusicDiscGoal.class);
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            if (event.getEntity() instanceof ServerPlayer other) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemInit.FREDDY_MICROPHONE.get())) {
                    FreddyUtils.addToGenericQuestGoal(player, DamagePlayersMicrophoneGoal.class, (int) event.getAmount() / 2);
                }
                if (MorphHolderAttacher.getCurrentMorph(other).isPresent()) {
                    QuestHolderAttacher.checkAllGoals(other, goal -> {
                        if (goal instanceof StayNearHunterGoal hunterGoal) {
                            if (!hunterGoal.isGoalMet()) hunterGoal.resetProgress();
                            else hunterGoal.addProgress(1);
                            return true;
                        }
                        return false;
                    });
                }
            }

            if (event.getEntity() instanceof Warden) {
                FreddyUtils.addToGenericQuestGoal(player, HitWardenGoal.class);
            }
            if (event.getEntity() instanceof ServerPlayer) {
                FreddyUtils.addToGenericQuestGoal(player, HitPlayersGoal.class);
            }
        }
    }

    @SubscribeEvent
    public static void onJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer entity) {
            var morph = MorphHolderAttacher.getCurrentMorphUnwrap(entity);
            if (morph != null) {
                morph.onMorphedTo(entity);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer player && event.phase == TickEvent.Phase.END) {
            FreddyHolderAttacher.getHolder(player).ifPresent(cap -> {
                if (MorphHolderAttacher.getCurrentMorph(player).isPresent()) {
                    if (player.tickCount % 20 == 0) {
                        var list = FreddyUtils.getEntitiesInRange(player, Player.class, 30, 25, 30, p -> p != player);
                        QuestHolderAttacher.checkAllGoals(player, goal -> {
                            if (goal instanceof StayNearHunterGoal hunterGoal) {
                                if (list.isEmpty() && !hunterGoal.isGoalMet()) {
                                    hunterGoal.resetProgress();
                                }
                                else {
                                    hunterGoal.addProgress(1);
                                }
                                return true;
                            }
                            return false;
                        });
                    }
                }
            });



/*            SkulkHolderAttacher.getSkulkHolder(player).ifPresent(cap -> {
                if (cap.getNettedInvulnTicks() > 0) {
                    cap.setNettedInvulnTicks(cap.getNettedInvulnTicks() - 1);
                }
                if (!cap.isInfinite()) {
                    if (player.tickCount % 20 == 0) {
                        cap.setSkulk(cap.getSkulk() + cap.getSkulkRegen());
                    }
                }
                else {
                    if (cap.getSkulk() != cap.getSkulkCap()) {
                        cap.setSkulk(cap.getSkulkCap());
                    }
                }
            });*/
        }
    }

    @SubscribeEvent
    public static void onCrit(CriticalHitEvent event) {

    }

    @SubscribeEvent
    public static void playerRightClick(PlayerInteractEvent event) {

    }

    @SubscribeEvent
    public static void netInteract(PlayerInteractEvent.EntityInteractSpecific event) {

    }

    @SubscribeEvent
    public static void onEffectGain(MobEffectEvent.Added event) {

    }

    @SubscribeEvent
    public static void playerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {


    }

    @SubscribeEvent
    public static void playerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (!event.getEntity().level().isClientSide) return;
    }

    @SubscribeEvent
    public static void playerAttackEntity(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide) return;
    }

    @SubscribeEvent
    public static void onTame(AnimalTameEvent event) {
        if (event.getTamer() instanceof ServerPlayer player) {
            QuestHolderAttacher.checkAllGoals(player, goal -> {
                if (goal instanceof TameEntityGoal tameEntityGoal) {
                    return tameEntityGoal.mobsTamed(event.getAnimal().getType());
                }
                return false;
            });
        }
    }
}
