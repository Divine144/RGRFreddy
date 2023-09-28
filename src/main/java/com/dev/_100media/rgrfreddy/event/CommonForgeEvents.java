package com.dev._100media.rgrfreddy.event;

import com.dev._100media.rgrfreddy.RGRFreddy;
import com.dev._100media.rgrfreddy.quest.goal.AquireAdvancementGoal;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.hundredmediaquests.cap.QuestHolderAttacher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SpawnerBlock;
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
    public static void onBlockBreak(BlockEvent.BreakEvent event) {

    }

    @SubscribeEvent
    public static void onVillagerTrade(TradeWithVillagerEvent event) {

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
    public static void onHurt(LivingHurtEvent event) {

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

   /*         SkulkHolderAttacher.getSkulkHolder(player).ifPresent(cap -> {
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
/*        if (event.getTamer() instanceof ServerPlayer player) {
            QuestHolderAttacher.checkAllGoals(player, goal -> {
                if (goal instanceof TameEntityGoal tameEntityGoal) {
                    return tameEntityGoal.mobsTamed(event.getAnimal().getType());
                }
                return false;
            });
        }*/
    }
}
