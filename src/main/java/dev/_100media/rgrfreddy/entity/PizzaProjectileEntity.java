package dev._100media.rgrfreddy.entity;

import dev._100media.rgrfreddy.init.ItemInit;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PizzaProjectileEntity extends ThrowableItemProjectile {

    private static final EntityDataAccessor<Integer> DATA_TARGET = SynchedEntityData.defineId(PizzaProjectileEntity.class, EntityDataSerializers.INT);
    private LivingEntity target;
    private static final DustParticleOptions YELLOW = new DustParticleOptions(Vec3.fromRGB24(14737920).toVector3f(), 1.2F);
    private LivingEntity owner;

    public PizzaProjectileEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.PIZZA_SLICE.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel level) {
            Vec3 vector3d1 = this.getDeltaMovement();
            double baseYOffset = 0.15D;
            level.sendParticles(YELLOW, this.getX() - vector3d1.x, this.getY() - (vector3d1.y + baseYOffset), this.getZ() - vector3d1.z, 1, 0, 0, 0, 0);
        }
        if (target == null) return;
        Vec3 vec3 = target.position().subtract(position()).normalize().scale(0.5);
        setDeltaMovement(vec3);
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.setPos(d2, d0, d1);
        updateRotation();
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_TARGET, -1);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (pKey == DATA_TARGET) {
            target = (LivingEntity) level().getEntity(entityData.get(DATA_TARGET));
        }
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
        entityData.set(DATA_TARGET, target.getId());
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    protected void updateRotation() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = vec3.horizontalDistance();
        this.setXRot(lerpRotation(this.xRotO, (float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI))));
        this.setYRot(lerpRotation(this.yRotO, (float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI))));
    }

    protected static float lerpRotation(float pCurrentRotation, float pTargetRotation) {
        while(pTargetRotation - pCurrentRotation < -180.0F) {
            pCurrentRotation -= 360.0F;
        }

        while(pTargetRotation - pCurrentRotation >= 180.0F) {
            pCurrentRotation += 360.0F;
        }

        return Mth.lerp(0.2F, pCurrentRotation, pTargetRotation);
    }
}
