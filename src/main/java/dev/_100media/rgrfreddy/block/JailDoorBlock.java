package dev._100media.rgrfreddy.block;

import dev._100media.rgrfreddy.block.entity.JailDoorBE;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(pContext)) {
            boolean flag = level.hasNeighborSignal(blockpos) || level.hasNeighborSignal(blockpos.above());
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(HINGE, this.getHinge(pContext)).setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, false).setValue(HALF, DoubleBlockHalf.LOWER);
        }
        else {
            return null;
        }
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        boolean flag = !pState.getValue(OPEN);
        boolean flag1 = pState.getValue(HINGE) == DoorHingeSide.RIGHT;
        switch (direction) {
            case EAST:
            default:
                return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
            case SOUTH:
                return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            case WEST:
                return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            case NORTH:
                return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
        }
    }

    private DoorHingeSide getHinge(BlockPlaceContext pContext) {
        BlockGetter blockgetter = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        Direction direction = pContext.getHorizontalDirection();
        BlockPos blockpos1 = blockpos.above();
        Direction direction1 = direction.getCounterClockWise();
        BlockPos blockpos2 = blockpos.relative(direction1);
        BlockState blockstate = blockgetter.getBlockState(blockpos2);
        BlockPos blockpos3 = blockpos1.relative(direction1);
        BlockState blockstate1 = blockgetter.getBlockState(blockpos3);
        Direction direction2 = direction.getClockWise();
        BlockPos blockpos4 = blockpos.relative(direction2);
        BlockState blockstate2 = blockgetter.getBlockState(blockpos4);
        BlockPos blockpos5 = blockpos1.relative(direction2);
        BlockState blockstate3 = blockgetter.getBlockState(blockpos5);
        int i = (blockstate.isCollisionShapeFullBlock(blockgetter, blockpos2) ? -1 : 0) + (blockstate1.isCollisionShapeFullBlock(blockgetter, blockpos3) ? -1 : 0) + (blockstate2.isCollisionShapeFullBlock(blockgetter, blockpos4) ? 1 : 0) + (blockstate3.isCollisionShapeFullBlock(blockgetter, blockpos5) ? 1 : 0);
        boolean flag = blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        boolean flag1 = blockstate2.is(this) && blockstate2.getValue(HALF) == DoubleBlockHalf.LOWER;
        if ((!flag || flag1) && i <= 0) {
            if ((!flag1 || flag) && i >= 0) {
                int j = direction.getStepX();
                int k = direction.getStepZ();
                Vec3 vec3 = pContext.getClickLocation();
                double d0 = vec3.x - (double)blockpos.getX();
                double d1 = vec3.z - (double)blockpos.getZ();
                return (j >= 0 || !(d1 < 0.5D)) && (j <= 0 || !(d1 > 0.5D)) && (k >= 0 || !(d0 > 0.5D)) && (k <= 0 || !(d0 < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
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
                    if (pState.hasProperty(HALF)) {
                        DoubleBlockHalf half = pState.getValue(HALF);
                        if (half == DoubleBlockHalf.UPPER) {
                            if (pLevel.getBlockEntity(pPos.below()) instanceof JailDoorBE lower) {
                                lower.setOpenTicks(20 * 5);
                            }
                        }
                        else {
                            if (pLevel.getBlockEntity(pPos.above()) instanceof JailDoorBE above) {
                                above.setOpenTicks(20 * 5);
                            }
                        }
                        be.setOpenTicks(20 * 5);
                    }
                    stack.shrink(1);
                    pLevel.playSound(null, pPos, SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.8f, 1f);
                    pState = pState.cycle(OPEN);
                    pLevel.setBlock(pPos, pState, 10);
                    this.playSound(pPlayer, pLevel, pPos, pState.getValue(OPEN));
                    pLevel.gameEvent(pPlayer, this.isOpen(pState) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
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
