package dev._100media.rgrfreddy.block;

import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.block.entity.MysticMusicBoxBE;
import dev._100media.rgrfreddy.block.entity.SnareBE;
import dev._100media.rgrfreddy.block.entity.ToyBoxTrapBE;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.EffectInit;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SnareBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;

    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape TOP_AABB = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public SnareBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(FACING, Direction.NORTH));
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof ServerPlayer player && player.level() instanceof ServerLevel level) {
            if (MorphHolderAttacher.getCurrentMorph(player).isEmpty() && pLevel.getBlockEntity(pPos) instanceof SnareBE be) {
                if (be.getTrappedPlayerUUID() == null) {
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, -1, 0, false, false, false));
                    player.addEffect(new MobEffectInstance(EffectInit.NETTED.get(), -1, 0, false, false, false));
                    level.players().forEach(pl -> {
                        pl.sendSystemMessage(Component.literal(player.getDisplayName().getString() + " has been trapped. Location: %d %d %d"
                                .formatted(player.getBlockX(), player.getBlockY(), player.getBlockZ())).withStyle(ChatFormatting.RED));
                    });
                    be.setTrappedPlayerUUID(player.getUUID());
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof SnareBE be) {
                if (be.getTrappedPlayerUUID() != null) {
                    Player player = pLevel.getPlayerByUUID(be.getTrappedPlayerUUID());
                    if (player != null) {
                        player.removeEffect(EffectInit.NETTED.get());
                        player.removeEffect(MobEffects.BLINDNESS);
                        pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        if (!pLevel.isClientSide && pProjectile instanceof AbstractArrow) {
            if (pLevel.getBlockEntity(pHit.getBlockPos()) instanceof SnareBE be) {
                if (be.getTrappedPlayerUUID() != null) {
                    Player player = pLevel.getPlayerByUUID(be.getTrappedPlayerUUID());
                    if (player != null) {
                        player.removeEffect(EffectInit.NETTED.get());
                        player.removeEffect(MobEffects.BLINDNESS);
                        pLevel.setBlockAndUpdate(pHit.getBlockPos(), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate1 = this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(FACING, pContext.getHorizontalDirection().getOpposite());
        Direction direction = pContext.getClickedFace();
        return direction != Direction.DOWN && (direction == Direction.UP || !(pContext.getClickLocation().y - (double)blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(TYPE, SlabType.TOP);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        SlabType slabtype = pState.getValue(TYPE);
        if (slabtype == SlabType.TOP) {
            return TOP_AABB;
        }
        return BOTTOM_AABB;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockInit.SNARE_BE.get(), this::tick) : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SnareBE(pPos, pState);
    }

    private void tick(Level level, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof SnareBE be) {
                if (be.getTrappedPlayerUUID() != null) {
                    Player player = serverLevel.getPlayerByUUID(be.getTrappedPlayerUUID());
                    if (player != null) {
                        level.playSound(null, blockPos, SoundInit.HEARTBEAT.get(), SoundSource.PLAYERS, 0.65f, 1f);
                    }
                }
            }
        }
    }
}
