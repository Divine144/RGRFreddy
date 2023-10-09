package dev._100media.rgrfreddy.block;

import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.block.entity.MysticMusicBoxBE;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SmokeBombBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public SmokeBombBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockInit.MYSTIC_MUSIC_BOX_BE.get(), this::tick) : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MysticMusicBoxBE(pPos, pState);
    }

    private void tick(Level level, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            if (level.getBlockEntity(blockPos) instanceof MysticMusicBoxBE be) {
                if (be.getTickCount() <= 0) {
                    level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                }
                else {
                    AABB aabb = FreddyUtils.AABBofSizeAccurate(blockPos, 4, 3, 4);
                    var list = FreddyUtils.getEntitiesInRange(blockPos, level, Player.class, 4, 3, 4, p -> MorphHolderAttacher.getCurrentMorph(p).isEmpty());
                    if (be.getTickCount() % 10 == 0) {
                        BlockPos.betweenClosedStream(aabb).forEach(pos -> {
                            serverLevel.sendParticles(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), 4, 0, 0, 0.5, 0.1);
                        });
                    }
                    for (Player player : list) {
                        if (player.getBoundingBox().intersects(aabb) && MorphHolderAttacher.getCurrentMorph(player).isEmpty()) {
                            if (!player.hasEffect(MobEffects.BLINDNESS)) {
                                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 20, 0, false, false, false));
                            }
                        }
                    }
                    be.decrementTickCount();
                }
            }
        }
    }
}
