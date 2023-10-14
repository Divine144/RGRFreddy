package dev._100media.rgrfreddy.block.entity;

import dev._100media.rgrfreddy.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class ToyBoxTrapBE extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private int timesHit = 0;
    private int firstTimeHit = -1;
    private int timer = 20 * 20; // 20 second initial timer
    private int tick;
    private UUID trappedPlayerUUID = null;

    public ToyBoxTrapBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockInit.TOY_BOX_TRAP_BE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (trappedPlayerUUID != null) {
            pTag.putUUID("playerUUID", this.trappedPlayerUUID);
        }
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.timer = pTag.getInt("timer");
        this.timesHit = pTag.getInt("timesHit");
        if (pTag.hasUUID("playerUUID")) {
            this.trappedPlayerUUID = pTag.getUUID("playerUUID");
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockInit.TOY_BOX_TRAP_BE.get();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void decrementTimer() {
        if (--timer < 0) {
            timer = 0;
        }
    }

    @Nullable
    public UUID getTrappedPlayerUUID() {
        return trappedPlayerUUID;
    }

    public void setTrappedPlayerUUID(UUID trappedPlayerUUID) {
        this.trappedPlayerUUID = trappedPlayerUUID;
    }

    public int getTimesHit() {
        // Reset if outside 20-second timer
        if (this.firstTimeHit == -1 || this.tick - this.firstTimeHit > 20 * 20) {
            this.firstTimeHit = -1;
            this.timesHit = 0;
            return 0;
        }

        return timesHit;
    }

    public void setTimesHit(int timesHit) {
        this.timesHit = timesHit;
    }

    public void incrementTimesHit() {
        // Reset if outside 20-second timer
        if (this.firstTimeHit == -1 || this.tick - this.firstTimeHit > 20 * 20) {
            this.firstTimeHit = this.tick;
            this.timesHit = 0;
        }

        if (this.timesHit == 3)
            return;

        this.timesHit++;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public int getTick() {
        return this.tick;
    }
}
