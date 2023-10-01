package dev._100media.rgrfreddy.block.entity;

import dev._100media.rgrfreddy.init.BlockInit;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DimensionalTrapDoorBE extends BlockEntity {

    private BlockPos linkedBlockPos = null;

    public DimensionalTrapDoorBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockInit.DIMENSIONAL_TRAPDOOR_BE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (linkedBlockPos != null) {
            pTag.put("linkedPos", NbtUtils.writeBlockPos(linkedBlockPos));
        }
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.get("linkedPos") instanceof CompoundTag tag) {
            linkedBlockPos = NbtUtils.readBlockPos(tag);
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
        return BlockInit.DIMENSIONAL_TRAPDOOR_BE.get();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public boolean shouldRenderFace(Direction pFace) {
        return pFace.getAxis() == Direction.Axis.Y;
    }

    @Nullable
    public BlockPos getLinkedBlockPos() {
        return linkedBlockPos;
    }

    public void setLinkedBlockPos(BlockPos linkedBlockPos) {
        this.linkedBlockPos = linkedBlockPos;
    }
}
