package dev._100media.rgrfreddy.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.hundredmediaquests.cap.QuestHolderAttacher;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.block.entity.DimensionalTrapDoorBE;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.cap.GlobalHolderAttacher;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.ItemInit;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.StopControllingPlayerPacket;
import dev._100media.rgrfreddy.quest.goal.*;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;

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
                .then(Commands.literal("heartbeat")
                        .then(Commands.argument("isEnabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean arg = BoolArgumentType.getBool(context, "isEnabled");
                                    GlobalHolderAttacher.getGlobalLevelCapability(context.getSource().getLevel()).ifPresent(c -> c.setShouldPlayHeartBeat(arg));
                                    return Command.SINGLE_SUCCESS;
                                })

                        )
                )
                .then(Commands.literal("toyTrapTeleportPos")
                        .then(Commands.argument("blockPos", BlockPosArgument.blockPos())
                                .executes(context -> {
                                    BlockPos pos = BlockPosArgument.getBlockPos(context, "blockPos");
                                    GlobalHolderAttacher.getGlobalLevelCapability(context.getSource().getLevel()).ifPresent(c -> c.setToyBoxTeleportPos(pos));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("control")
                        .then(Commands.argument("controllingPlayer", EntityArgument.player())
                                .then(Commands.argument("toBeControlled", EntityArgument.player())
                                        .executes(context -> {
                                            Player controller = EntityArgument.getPlayer(context, "controllingPlayer");
                                            Player controlee = EntityArgument.getPlayer(context, "toBeControlled");
                                            FreddyHolderAttacher.getHolder(controlee).ifPresent(p -> {
                                                p.setControllingPlayer(controller.getUUID());
                                                p.setControlTicks(20 * 60);
                                                FreddyHolderAttacher.getHolder(controller).ifPresent(o -> {
                                                    o.setControlledPlayer(controlee.getUUID());
                                                    o.setControlTicks(20 * 60);
                                                });
                                            });
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
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
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            if (event.getState().is(BlockInit.EMP_GENERATOR_BLOCK.get())) {
                var holder = FreddyHolderAttacher.getHolderUnwrap(player);
                if (holder != null && holder.isHasPlacedGenerator()) {
                    holder.setHasPlacedGenerator(false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var holder = FreddyHolderAttacher.getHolderUnwrap(player);
            if (holder != null) {
                if (event.getPlacedBlock().is(BlockInit.DIMENSIONAL_TRAPDOOR_BLOCK.get())) {
                    // Check if player has morph
                    BlockPos holderPos = holder.getLastPortalBlockPos();
                    if (player.level().getBlockEntity(event.getPos()) instanceof DimensionalTrapDoorBE be) {
                        if (player.level().getBlockEntity(holderPos) instanceof DimensionalTrapDoorBE other) {
                            be.setLinkedBlockPos(holderPos);
                            other.setLinkedBlockPos(event.getPos());
                            holder.setLastPortalBlockPos(BlockPos.ZERO);
                            player.sendSystemMessage(Component.literal("Linked to: %s %s %s"
                                    .formatted(holderPos.getX(), holderPos.getY(), holderPos.getZ())).withStyle(ChatFormatting.GREEN), true);
                        }
                        else {
                            holder.setLastPortalBlockPos(event.getPos());
                            player.sendSystemMessage(Component.literal("Ready for Link at: %s %s %s"
                                            .formatted(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()))
                                    .withStyle(ChatFormatting.GREEN), true);
                        }
                    }

                }
                else if (event.getPlacedBlock().is(BlockInit.EMP_GENERATOR_BLOCK.get())) {
                    if (holder.isHasPlacedGenerator()) {
                        player.sendSystemMessage(Component.literal("You can only place one generator at a time!").withStyle(ChatFormatting.RED), true);
                        event.setCanceled(true);
                    }
                    else holder.setHasPlacedGenerator(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Player controllingPlayer = FreddyUtils.getControllingPlayer(player);
            if (controllingPlayer instanceof ServerPlayer controllingServerPlayer) {
                if (event.getSource().is(DamageTypes.LAVA)) {
                    FreddyUtils.addToGenericQuestGoal(controllingServerPlayer, ControlPlayerToLavaGoal.class);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {

    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player && player.getItemBySlot(EquipmentSlot.HEAD).is(ItemInit.GUARD_MASK.get())) {
            MobEffectInstance instance = event.getEffectInstance();
            if (instance != null && instance.getEffect() == MobEffects.BLINDNESS) {
                event.setResult(Event.Result.DENY);
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
            if (player.tickCount % 20 == 0) {
                if (player.getInventory().contains(ItemTags.MUSIC_DISCS)) {
                    FreddyUtils.addToGenericQuestGoal(player, AquireMusicDiscGoal.class);
                }
                else {
                    FreddyUtils.addToGenericQuestGoal(player, AquireMusicDiscGoal.class, -1);
                }
                Optional<ItemStack> goldStack = player.getInventory().items.stream()
                        .filter(ItemStack::isEnchanted)
                        .filter(stack -> stack.getItem() instanceof TieredItem item && item.getTier() == Tiers.GOLD ||
                                stack.getItem() instanceof ArmorItem item1 && item1.getMaterial() == ArmorMaterials.GOLD)
                        .findAny();
                if (goldStack.isPresent()) {
                    FreddyUtils.addToGenericQuestGoal(player, AquireGoldenItemGoal.class);
                }
                else FreddyUtils.addToGenericQuestGoal(player, AquireGoldenItemGoal.class, -1);
            }
            FreddyHolderAttacher.getHolder(player).ifPresent(cap -> {
                if (cap.getLastTeleportTicks() > 0) {
                    cap.setLastTeleportTicks(cap.getLastTeleportTicks() - 1);
                }
                if (cap.getFearTicks() > 0) {
                    cap.decrementFearTicks();
                }
                if (cap.getControlTicks() > 0) {
                    cap.decrementControlTicks();
                }
                else {
                    Player controllingPlayer = cap.getControllingPlayer();
                    Player controlledPlayer = cap.getControlledPlayer();
                    if (controllingPlayer != null) {
                        if (controllingPlayer instanceof ServerPlayer serverPlayer) {
                            FreddyHolderAttacher.getHolder(serverPlayer).ifPresent(p -> p.setControlledPlayer(null));
                            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new StopControllingPlayerPacket());
                        }
                        cap.setControllingPlayer(null);
                    }
                    else if (controlledPlayer != null) {
                        if (controlledPlayer instanceof ServerPlayer serverPlayer) {
                            FreddyHolderAttacher.getHolder(serverPlayer).ifPresent(p -> p.setControllingPlayer(null));
                        }
                        ItemStack helmetStack = controlledPlayer.getItemBySlot(EquipmentSlot.HEAD);
                        if (helmetStack.is(ItemInit.FREDDY_HAT.get()))
                            controlledPlayer.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        cap.setControlledPlayer(null);
                        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new StopControllingPlayerPacket());
                    }
                }
                if (cap.getJumpscareBlockTicks() > 0) {
                    cap.decrementJumpscareBlockTicks();
                }
                if (MorphHolderAttacher.getCurrentMorph(player).isPresent()) {
                    var list = FreddyUtils.getEntitiesInRange(player, Player.class, 30, 25, 30, p -> p != player);
                    var globalHolder = GlobalHolderAttacher.getGlobalLevelCapabilityUnwrap(player.serverLevel());
                    if (globalHolder != null && globalHolder.isShouldPlayHeartBeat()) {
                        if (player.tickCount % 12 == 0 && !list.isEmpty()) {
                            player.serverLevel().playSound(null, player.blockPosition(), SoundInit.HEARTBEAT.get(), SoundSource.PLAYERS, 0.65f, 1f);
                        }
                    }
                    if (player.tickCount % 20 == 0) {
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
