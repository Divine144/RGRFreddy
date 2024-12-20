package dev._100media.rgrfreddy.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.entity.PizzaProjectileEntity;
import dev._100media.rgrfreddy.init.EntityInit;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class FreddyPizzaItem extends Item implements GeoItem {

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    public FreddyPizzaItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        ItemStack itemStack = player.getItemInHand(pUsedHand);
        player.startUsingItem(pUsedHand);
        if (pLevel.isClientSide)
            return InteractionResultHolder.pass(itemStack);

        addMissile(pLevel, player);

        return InteractionResultHolder.consume(itemStack);
    }

    private static void addMissile(Level pLevel, Player player) {
        var list = FreddyUtils.getEntitiesInRange(player, LivingEntity.class, 20, 20, 20, p -> p != player);
        PizzaProjectileEntity missile = new PizzaProjectileEntity(EntityInit.PIZZA.get(), pLevel);
        missile.setPos(player.getEyePosition());
        missile.setOwner(player);
        missile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 0.8F, 0);
        if (!list.isEmpty()) {
            missile.setTarget(list.get(0));
        }
        pLevel.addFreshEntity(missile);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level level, LivingEntity livingEntity, int pTimeCharged) {
        if (!level.isClientSide && /*FMLEnvironment.production && */livingEntity instanceof Player player)
            player.getCooldowns().addCooldown(this, FMLEnvironment.production ? 30 * 20 : 5 * 20);

        super.releaseUsing(pStack, level, livingEntity, pTimeCharged);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (!pLevel.isClientSide && pLivingEntity instanceof ServerPlayer player && pRemainingUseDuration % 5 == 0) {
            addMissile(pLevel, player);
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GeoItemRenderer<FreddyPizzaItem>(new DefaultedItemGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "pizza")));
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    public void shootFromRotation(Entity entity, Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float) Math.PI / 180F)) * Mth.cos(pX * ((float) Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float) Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float) Math.PI / 180F)) * Mth.cos(pX * ((float) Math.PI / 180F));
        this.shoot(entity, f, f1, f2, pVelocity, pInaccuracy);
        Vec3 vec3 = pShooter.getDeltaMovement();
        entity.setDeltaMovement(entity.getDeltaMovement().add(vec3.x, pShooter.onGround() ? 0.0D : vec3.y, vec3.z));
    }

    public void shoot(Entity entity, double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().add(entity.level().random.triangle(0.0D, 0.0172275D * (double) pInaccuracy), entity.level().random.triangle(0.0D,
                0.0172275D * (double) pInaccuracy), entity.level().random.triangle(0.0D, 0.0172275D * (double) pInaccuracy)).scale((double) pVelocity);
        entity.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        entity.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
        entity.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
        entity.yRotO = entity.getYRot();
        entity.xRotO = entity.getXRot();
    }
}
