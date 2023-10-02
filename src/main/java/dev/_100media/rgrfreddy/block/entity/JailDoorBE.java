package dev._100media.rgrfreddy.block.entity;

import dev._100media.rgrfreddy.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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

public class JailDoorBE extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation OPEN = RawAnimation.begin().thenPlayAndHold("open");
    private static final RawAnimation CLOSE = RawAnimation.begin().thenPlayAndHold("close");
    private int openTicks = 0;

    public JailDoorBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockInit.JAIL_DOOR_BE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("openTicks", this.openTicks);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.openTicks = pTag.getInt("openTicks");
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
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, event -> {
            var controller = event.getController();
            var currentAnimation = controller.getCurrentAnimation();
            if (event.getData(DataTickets.BLOCK_ENTITY) instanceof JailDoorBE be) {
                BlockState state = be.getBlockState();
                if (state.hasProperty(DoorBlock.OPEN)) {
                    if (currentAnimation != null && state.getValue(DoorBlock.OPEN)) {
                        Animation animation = currentAnimation.animation();
                        if (animation != null && !animation.name().contains("open")) {
                            return event.setAndContinue(OPEN);
                        }
                    }
                    else if (currentAnimation == null || currentAnimation.animation() == null || !currentAnimation.animation().name().contains("close")) {
                        return event.setAndContinue(CLOSE);
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

    public int getOpenTicks() {
        return openTicks;
    }

    public void decrementOpenTicks() {
        if (--openTicks < 0) {
            openTicks = 0;
        }
    }

    public void setOpenTicks(int openTicks) {
        this.openTicks = openTicks;
        if (openTicks < 0) openTicks = 0;
    }
}
