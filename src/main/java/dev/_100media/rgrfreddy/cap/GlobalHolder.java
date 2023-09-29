package dev._100media.rgrfreddy.cap;

import dev._100media.capabilitysyncer.core.GlobalLevelCapability;
import dev._100media.capabilitysyncer.network.LevelCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleLevelCapabilityStatusPacket;
import dev._100media.rgrfreddy.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.simple.SimpleChannel;

public class GlobalHolder extends GlobalLevelCapability {

    private boolean shouldPlayHeartBeat = true;
    private BlockPos toyBoxTeleportPos = BlockPos.ZERO;

    protected GlobalHolder(Level level) {
        super(level);
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("heartbeat", this.shouldPlayHeartBeat);
        tag.put("blockPos", NbtUtils.writeBlockPos(toyBoxTeleportPos));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.shouldPlayHeartBeat = nbt.getBoolean("heartbeat");
        if (nbt.get("blockPos") instanceof CompoundTag tag) {
            this.toyBoxTeleportPos = NbtUtils.readBlockPos(tag);
        }
    }

    @Override
    public LevelCapabilityStatusPacket createUpdatePacket() {
        return new SimpleLevelCapabilityStatusPacket(GlobalHolderAttacher.EXAMPLE_GLOBAL_LEVEL_CAPABILITY_RL, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }

    public boolean isShouldPlayHeartBeat() {
        return shouldPlayHeartBeat;
    }

    public void setShouldPlayHeartBeat(boolean shouldPlayHeartBeat) {
        this.shouldPlayHeartBeat = shouldPlayHeartBeat;
    }

    public BlockPos getToyBoxTeleportPos() {
        return toyBoxTeleportPos;
    }

    public void setToyBoxTeleportPos(BlockPos toyBoxTeleportPos) {
        this.toyBoxTeleportPos = toyBoxTeleportPos;
    }
}
