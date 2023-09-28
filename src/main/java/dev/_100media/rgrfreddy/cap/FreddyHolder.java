package dev._100media.rgrfreddy.cap;

import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.capabilitysyncer.core.PlayerCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.simple.SimpleChannel;

public class FreddyHolder extends PlayerCapability {
    private int timeNearHunters;

    protected FreddyHolder(Player entity) {
        super(entity);
    }

    public int getTimeNearHunters() {
        return this.timeNearHunters;
    }

    public void setTimeNearHunters(int timeNearHunters) {
        this.timeNearHunters = timeNearHunters;
        updateTracking();
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("timeNearHunters", this.timeNearHunters);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.timeNearHunters = nbt.getInt("timeNearHunters");
    }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.entity.getId(), FreddyHolderAttacher.LOCATION, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }
}
