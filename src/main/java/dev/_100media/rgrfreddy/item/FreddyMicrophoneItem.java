package dev._100media.rgrfreddy.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FreddyMicrophoneItem extends Item {

    private final ImmutableMultimap<Attribute, AttributeModifier> modifierMap = ImmutableMultimap.<Attribute, AttributeModifier>builder()
            .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0f + Tiers.DIAMOND.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION))
            .build();

    public FreddyMicrophoneItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.consume(itemStack);
        }
        if (MorphHolderAttacher.getCurrentMorph(pPlayer).isPresent()) {
            if (pPlayer instanceof ServerPlayer player) {
                player.level().playSound(null, player.blockPosition(), SoundInit.MICROPHONE.get(), SoundSource.PLAYERS, 0.6f, 1f);
                var list = FreddyUtils.getEntitiesInRange(pPlayer, ServerPlayer.class, 10, 10, 10, p -> p != pPlayer);
                list.forEach(player1 -> {
                    player1.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 10, 0, false, false, false));
                });
                player.getCooldowns().addCooldown(this, 20 * 30);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? modifierMap : ImmutableMultimap.of();
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (pState.is(Blocks.COBWEB)) {
            return 15.0F;
        }
        else {
            return pState.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }
}
