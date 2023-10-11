package dev._100media.rgrfreddy.block.entity;

import dev._100media.rgrfreddy.block.SmokeBombBlock;
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
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MysticMusicBoxBE extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation OPEN = RawAnimation.begin().then("open", Animation.LoopType.PLAY_ONCE).then("open_still", Animation.LoopType.PLAY_ONCE).thenLoop("open_playing");

    private int tickCount = 20 * 20;

    public MysticMusicBoxBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockInit.MYSTIC_MUSIC_BOX_BE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("tickCount", this.tickCount);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.tickCount = pTag.getInt("tickCount");
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
        return BlockInit.MYSTIC_MUSIC_BOX_BE.get();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, event -> {
            var controller = event.getController();
            var currentAnimation = controller.getCurrentAnimation();
            if (event.getData(DataTickets.BLOCK_ENTITY) instanceof MysticMusicBoxBE be) {
                BlockState state = be.getBlockState();
                if (!(state.getBlock() instanceof SmokeBombBlock)) {
                    if (currentAnimation == null || currentAnimation.animation() == null || !currentAnimation.animation().name().contains("open")) {
                        return event.setAndContinue(OPEN);
                    }
                }
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    public int getTickCount() {
        return tickCount;
    }

    public void decrementTickCount() {
        if (--tickCount < 0) {
            tickCount = 0;
        }
    }

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }
}
