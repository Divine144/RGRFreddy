package dev._100media.rgrfreddy.entity;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.init.ItemInit;
import dev._100media.rgrfreddy.network.ClientHandler;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.StartControllingPlayerPacket;
import dev._100media.rgrfreddy.util.FreddyHatCameraManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FreddyHatProjectileEntity extends ThrowableProjectile implements GeoEntity {

    private boolean added = false;
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public FreddyHatProjectileEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

  /*  public FreddyHatProjectileEntity(Level level, Player owner) {
        super(EntityInit.RASEN_SHURIKEN.get(), level);
        setOwner(owner);
    }*/

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && !added) {
            FreddyHatCameraManager.add(this);
            added = true;
        }
        if (!level().isClientSide && this.getOwner() != null && this.distanceTo(getOwner()) >= 50) {
            discard();
            return;
        }
        setDeltaMovement(calculateViewVector(getXRot(), getYRot()).scale(1));
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (level().isClientSide) {
            return;
        }
        if (pResult.getEntity() instanceof Player player) {
            ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
            if (stack.is(Items.AIR)) {
                player.getInventory().add(stack.copyAndClear());
            }
            if (this.getOwner() instanceof ServerPlayer serverPlayer) {
                FreddyHolderAttacher.getHolder(player).ifPresent(p -> {
                    p.setControllingPlayer(this.getOwner().getUUID());
                    p.setControlTicks(20 * 60);
                    p.setLeftControl(false);
                    FreddyHolderAttacher.getHolder(this.getOwner()).ifPresent(o -> {
                        o.setControlledPlayer(player.getUUID());
                        o.setControlTicks(20 * 60);
                        o.setLeftControl(false);
                    });
                });
                player.setItemSlot(EquipmentSlot.HEAD, ItemInit.FREDDY_HAT.get().getDefaultInstance());
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new StartControllingPlayerPacket());
                discard();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (level().isClientSide) {
            return;
        }
        discard();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public float getViewXRot(float pPartialTicks) {
        if (level().isClientSide) {
            return ClientHandler.getPlayer().getViewXRot(pPartialTicks);
        }
        return super.getViewXRot(pPartialTicks);
    }

    @Override
    public float getViewYRot(float pPartialTicks) {
        if (level().isClientSide) {
            return ClientHandler.getPlayer().getViewYRot(pPartialTicks);
        }
        return super.getViewYRot(pPartialTicks);
    }

    @Override
    public void remove(RemovalReason pReason) {
        super.remove(pReason);
        if (level().isClientSide) {
            FreddyHatCameraManager.remove();
        }
    }

    @Override
    protected void updateRotation() {
        if (getOwner() != null) {
            setXRot(getOwner().getXRot());
            setYRot(getOwner().getYRot());
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
}
