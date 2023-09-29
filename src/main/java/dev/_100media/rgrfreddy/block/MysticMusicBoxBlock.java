package dev._100media.rgrfreddy.block;

import dev._100media.hundredmediaabilities.capability.MarkerHolderAttacher;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.hundredmediamorphs.morph.Morph;
import dev._100media.rgrfreddy.block.entity.MysticMusicBoxBE;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.MarkerInit;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.UnboundControlsPacket;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class MysticMusicBoxBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public MysticMusicBoxBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
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

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BOTTOM_AABB;
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
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof MysticMusicBoxBE) {
                var list = FreddyUtils.getEntitiesInRange(blockPos, level, ServerPlayer.class, 20, 20, 20, p -> true);
                list.forEach(player -> {
                    if (MorphHolderAttacher.getCurrentMorph(player).isEmpty()) {
                        MarkerHolderAttacher.getMarkerHolder(player).ifPresent(holder -> {
                            if (!holder.hasMarker(MarkerInit.UNBOUND_CONTROLS_MARKER.get())) {
                                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UnboundControlsPacket(false));
                                holder.addMarker(MarkerInit.UNBOUND_CONTROLS_MARKER.get(), true);
                            }
                        });
                    }
                });
                ((ServerLevel) level).players().forEach(player -> {
                    if (!list.contains(player)) { // If the player is out of the box's range
                        Morph morph = MorphHolderAttacher.getCurrentMorphUnwrap(player);
                        if (morph != null) {
                            // TODO: do only once
                            player.removeAllEffects();
                            morph.onMorphedTo(player);
                        }
                        MarkerHolderAttacher.getMarkerHolder(player).ifPresent(holder -> {
                            if (holder.hasMarker(MarkerInit.UNBOUND_CONTROLS_MARKER.get())) {
                                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UnboundControlsPacket(true));
                                holder.removeMarker(MarkerInit.UNBOUND_CONTROLS_MARKER.get(), true);
                            }
                        });
                    }
                });
            }
        }
    }
}
