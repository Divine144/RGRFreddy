package dev._100media.rgrfreddy.entity;

import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.init.EntityInit;
import dev._100media.rgrfreddy.init.ItemInit;
import dev._100media.rgrfreddy.network.ClientHandler;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.StartControllingPlayerPacket;
import dev._100media.rgrfreddy.util.FreddyHatCameraManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.fml.loading.FMLEnvironment;
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

    public FreddyHatProjectileEntity(Level level, Player owner) {
        super(EntityInit.FREDDY_HAT_PROJECTILE.get(), level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1D, owner.getZ());
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && !added) {
            FreddyHatCameraManager.add(this);
            added = true;
        }
        setDeltaMovement(calculateViewVector(getXRot(), getYRot()).scale(1.8));
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (level().isClientSide)
            return;

        if (pResult.getEntity() instanceof Player player)
            this.attachToPlayer(player);

        this.discard();
    }

    private void attachToPlayer(Player player) {
        if (!(this.getOwner() instanceof ServerPlayer owner))
            return;

        ItemStack oldHelmetStack = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!oldHelmetStack.is(Items.AIR))
            player.getInventory().add(oldHelmetStack.copyAndClear());

        FreddyHolderAttacher.getHolder(player).ifPresent(p -> {
            p.setControllingPlayer(owner.getUUID());
            p.setControlTicks(FMLEnvironment.production ? 60 * 20 : 20 * 20);
            FreddyHolderAttacher.getHolder(owner).ifPresent(o -> {
                o.setControlledPlayer(player.getUUID());
                o.setControlTicks(FMLEnvironment.production ? 60 * 20 : 20 * 20);
            });
        });

        if (FMLEnvironment.production)
            owner.getCooldowns().addCooldown(ItemInit.FREDDY_HAT.get(), 120 * 20);

        player.setItemSlot(EquipmentSlot.HEAD, ItemInit.FREDDY_HAT.get().getDefaultInstance());
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> owner), new StartControllingPlayerPacket());
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (level().isClientSide)
            return;

        AABB aabb = new AABB(this.position(), this.position().add(1, 1, 1)).inflate(2);
        for (Entity entity : this.level().getEntities(this.getOwner(), aabb, EntitySelector.NO_SPECTATORS)) {
            if (entity instanceof Player player) {
                this.attachToPlayer(player);
                break;
            }
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
