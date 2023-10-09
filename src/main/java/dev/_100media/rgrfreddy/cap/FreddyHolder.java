package dev._100media.rgrfreddy.cap;

import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.capabilitysyncer.core.PlayerCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;

public class FreddyHolder extends PlayerCapability {

    private int lastTeleportTicks;
    private BlockPos lastPortalBlockPos = BlockPos.ZERO;
    private boolean hasPlacedGenerator = false;
    private boolean abilitiesDisabled = false;
    private int fearTicks;
    private int jumpscareBlockTicks = 0;
    private UUID controllingPlayer = null;
    private UUID controlledPlayer = null;
    private int controlTicks = 0;

    protected FreddyHolder(Player entity) {
        super(entity);
    }

    public int getLastTeleportTicks() {
        return this.lastTeleportTicks;
    }

    public void setLastTeleportTicks(int lastTeleportTicks) {
        this.lastTeleportTicks = lastTeleportTicks;
        if (this.lastTeleportTicks < 0) this.lastTeleportTicks = 0;
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("timeNearHunters", this.lastTeleportTicks);
        tag.put("linkedBlockPos", NbtUtils.writeBlockPos(lastPortalBlockPos));
        tag.putBoolean("abilitiesDisabled", this.abilitiesDisabled);
        tag.putBoolean("hasPlacedGenerator", this.hasPlacedGenerator);
        tag.putInt("fearTicks", this.fearTicks);
        if (controllingPlayer != null) {
            tag.putUUID("controllingPlayer", controllingPlayer);
        }
        if (controlledPlayer != null) {
            tag.putUUID("controlledPlayer", controlledPlayer);
        }
        tag.putInt("controlTicks", this.controlTicks);
        tag.putInt("jump", this.jumpscareBlockTicks);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.lastTeleportTicks = nbt.getInt("timeNearHunters");
        if (nbt.get("linkedBlockPos") instanceof CompoundTag tag) {
            lastPortalBlockPos = NbtUtils.readBlockPos(tag);
        }
        this.abilitiesDisabled = nbt.getBoolean("abilitiesDisabled");
        this.hasPlacedGenerator = nbt.getBoolean("hasPlacedGenerator");
        this.fearTicks = nbt.getInt("fearTicks");
        if (nbt.hasUUID("controllingPlayer")) {
            this.controllingPlayer = nbt.getUUID("controllingPlayer");
        }
        if (nbt.hasUUID("controlledPlayer")) {
            this.controlledPlayer = nbt.getUUID("controlledPlayer");
        }
        this.controlTicks = nbt.getInt("controlTicks");
        this.jumpscareBlockTicks = nbt.getInt("jump");
    }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.entity.getId(), FreddyHolderAttacher.LOCATION, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }

    public BlockPos getLastPortalBlockPos() {
        return lastPortalBlockPos;
    }

    public void setLastPortalBlockPos(BlockPos lastPortalBlockPos) {
        this.lastPortalBlockPos = lastPortalBlockPos;
    }

    public boolean isAbilitiesDisabled() {
        return abilitiesDisabled;
    }

    public void setAbilitiesDisabled(boolean abilitiesDisabled) {
        this.abilitiesDisabled = abilitiesDisabled;
        updateTracking();
    }

    public boolean isHasPlacedGenerator() {
        return hasPlacedGenerator;
    }

    public void setHasPlacedGenerator(boolean hasPlacedGenerator) {
        this.hasPlacedGenerator = hasPlacedGenerator;
    }

    public int getFearTicks() {
        return fearTicks;
    }

    public void decrementFearTicks() {
        if (--fearTicks == 0) {
            updateTracking();
        }
        else if (fearTicks < 0) this.fearTicks = 0;
    }

    public void setFearTicks(int fearTicks) {
        this.fearTicks = fearTicks;
        updateTracking();
    }

    public UUID getControllingPlayer() {
        return controllingPlayer;
    }

    public void setControllingPlayer(UUID controllingPlayer) {
        this.controllingPlayer = controllingPlayer;
        updateTracking();
    }

    public UUID getControlledPlayer() {
        return controlledPlayer;
    }

    public void setControlledPlayer(UUID controlledPlayer) {
        this.controlledPlayer = controlledPlayer;
        updateTracking();
    }

    public int getControlTicks() {
        return controlTicks;
    }

    public void decrementControlTicks() {
        if (--controlTicks == 0) {
            updateTracking();
        }
        else if (controlTicks < 0) this.controlTicks = 0;
    }

    public void setControlTicks(int controlTicks) {
        this.controlTicks = controlTicks;
        updateTracking();
    }

    public int getJumpscareBlockTicks() {
        return jumpscareBlockTicks;
    }

    public void decrementJumpscareBlockTicks() {
        if (--jumpscareBlockTicks == 0) {}
        else if (jumpscareBlockTicks < 0) this.jumpscareBlockTicks = 0;
    }

    public void setJumpscareBlockTicks(int jumpscareBlockTicks) {
        this.jumpscareBlockTicks = jumpscareBlockTicks;
    }
}
