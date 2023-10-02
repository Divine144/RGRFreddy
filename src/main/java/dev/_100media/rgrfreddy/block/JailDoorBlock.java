package dev._100media.rgrfreddy.block;

import dev._100media.rgrfreddy.block.entity.JailDoorBE;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class JailDoorBlock extends DoorBlock implements EntityBlock {

    public JailDoorBlock(Properties pProperties) {
        super(pProperties, BlockSetType.IRON);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new JailDoorBE(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer instanceof ServerPlayer player) {
            ItemStack stack = player.getItemInHand(pHand);
            if (stack.is(ItemInit.PIZZERIA_KEY.get())) {
                if (pLevel.getBlockEntity(pPos) instanceof JailDoorBE be && pState.hasProperty(OPEN) && !pState.getValue(OPEN)) {
                    be.setOpenTicks(20 * 5);
                    stack.shrink(1);
                    pLevel.playSound(null, pPos, SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.8f, 1f);
                    return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
                }
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockInit.JAIL_DOOR_BE.get(), this::tick) : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    private void tick(Level level, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof JailDoorBE be) {
                if (be.getOpenTicks() > 0) {
                    be.decrementOpenTicks();
                }
                else if (state.hasProperty(OPEN) && state.getValue(OPEN)) {
                    state = state.cycle(OPEN);
                    level.setBlock(blockPos, state, 10);
                    this.playSound(null, serverLevel, blockPos, state.getValue(OPEN));
                }
            }
        }
    }

    private void playSound(@Nullable Entity pSource, Level pLevel, BlockPos pPos, boolean pIsOpening) {
        pLevel.playSound(pSource, pPos, pIsOpening ? BlockSetType.IRON.doorOpen() : BlockSetType.IRON.doorClose(), SoundSource.BLOCKS, 1.0F, pLevel.getRandom().nextFloat() * 0.1F + 0.9F);
    }
}
