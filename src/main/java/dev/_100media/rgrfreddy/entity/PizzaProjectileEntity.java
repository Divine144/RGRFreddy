package dev._100media.rgrfreddy.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PizzaProjectileEntity extends ThrowableProjectile implements GeoEntity {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public PizzaProjectileEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (this.getOwner() != null && this.distanceTo(getOwner()) >= 50) {
                discard();
            }
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (pResult instanceof EntityHitResult result && result.getEntity() == this.getOwner()) return;
        super.onHit(pResult);
        if (!level().isClientSide) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!level().isClientSide && pResult.getEntity() != this.getOwner() && pResult.getEntity() instanceof LivingEntity living) {
            living.hurt(this.damageSources().mobProjectile(this, getOwner() instanceof LivingEntity l ? l : null), 6.0f);
            level().explode(this, this.getX(), this.getY(), this.getZ(), 4f, Level.ExplosionInteraction.TNT);
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
