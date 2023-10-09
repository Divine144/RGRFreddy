package dev._100media.rgrfreddy.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class FreddyMicrophoneItem extends Item implements GeoItem {

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GeoItemRenderer<FreddyMicrophoneItem>(new DefaultedItemGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "freddy_microphone"))) {
                        @Override
                        public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
                            poseStack.pushPose();
                            switch (transformType) {
                                case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> {
                                    poseStack.translate(-0.07, -0.2, -0.3);
                                }
                            }
                            super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
                            poseStack.popPose();
                        }

                        @Override
                        protected void renderInGui(ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
                            poseStack.pushPose();
                            poseStack.mulPose(Axis.YP.rotationDegrees(90));
                            poseStack.scale(1.2f, 1.2f, 1.2f);
                            poseStack.translate(0.05, -0.25, -0.1);
                            super.renderInGui(transformType, poseStack, bufferSource, packedLight, packedOverlay);
                            poseStack.popPose();
                        }
                    };
                return this.renderer;
            }
        });
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }
}
