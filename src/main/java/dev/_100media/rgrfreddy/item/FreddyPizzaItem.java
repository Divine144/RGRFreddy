package dev._100media.rgrfreddy.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.init.EntityInit;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class FreddyPizzaItem extends Item implements GeoItem {

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    public FreddyPizzaItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.pass(itemStack);

        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GeoItemRenderer<FreddyPizzaItem>(new DefaultedItemGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "pizza"))) {
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
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (!pLevel.isClientSide) {
            if (pLivingEntity instanceof ServerPlayer player) {
                if (pRemainingUseDuration % 2 == 0) {
                    var entity = EntityInit.PIZZA.get().create(pLevel);
                    if (entity != null) {
                        entity.setPos(player.getX(), player.getEyeY() - 0.55, player.getZ());
                        entity.setOwner(player);
                        entity.setNoGravity(true);
                        entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
                        entity.setYRot(-Mth.wrapDegrees(player.getYRot()));
                        entity.setXRot(-Mth.wrapDegrees(player.getXRot()));
                        entity.xRotO = -Mth.wrapDegrees(player.xRotO);
                        entity.yRotO = -Mth.wrapDegrees(player.yRotO);
                        player.level().addFreshEntity(entity);
                    }
                }
            }
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }
}
