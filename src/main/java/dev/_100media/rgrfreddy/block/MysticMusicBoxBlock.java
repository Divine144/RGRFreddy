package dev._100media.rgrfreddy.block;

import dev._100media.hundredmediaabilities.capability.MarkerHolderAttacher;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.hundredmediamorphs.morph.Morph;
import dev._100media.rgrfreddy.block.entity.MysticMusicBoxBE;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.MarkerInit;
import dev._100media.rgrfreddy.init.MorphInit;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.UnboundControlsPacket;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MysticMusicBoxBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public MysticMusicBoxBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.players().forEach(MysticMusicBoxBlock::removeEffects);
        }
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
        if (!(level instanceof ServerLevel serverLevel))
            return;

        if (!(level.getBlockEntity(blockPos) instanceof MysticMusicBoxBE be))
            return;

        if (be.getTickCount() == 1) {
            serverLevel.players().forEach(MysticMusicBoxBlock::removeEffects);

            serverLevel.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
            return;
        }

        if (serverLevel.getServer().getTickCount() % 100 == 0) {
            serverLevel.playSound(null, blockPos, SoundInit.MUSIC_BOX.get(), SoundSource.PLAYERS, 0.65f, 1f);
        }
        var list = FreddyUtils.getEntitiesInRange(blockPos, level, ServerPlayer.class, 20, 20, 20, p -> true);
        list.forEach(MysticMusicBoxBlock::addEffect);

        // If the player is out of the box's range (we do the logic here for ease of access I guess)
        serverLevel.players().stream().filter(player -> !list.contains(player)).forEach(MysticMusicBoxBlock::removeEffects);

        be.decrementTickCount();
    }

    private static void addEffect(ServerPlayer player) {
        var markerHolder = MarkerHolderAttacher.getMarkerHolderUnwrap(player);
        if (markerHolder != null) {
            if (MorphHolderAttacher.getCurrentMorph(player).isEmpty()) {
                MarkerHolderAttacher.getMarkerHolder(player).ifPresent(holder -> {
                    if (!holder.hasMarker(MarkerInit.UNBOUND_CONTROLS_MARKER.get())) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 1, false, false, false));
                        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UnboundControlsPacket(false));
                        holder.addMarker(MarkerInit.UNBOUND_CONTROLS_MARKER.get(), false);
                    }
                });
            }
            else {
                var effect = player.getEffect(MobEffects.MOVEMENT_SPEED);
                if (effect != null) {
                    if (effect.getAmplifier() < 9) {
                        player.removeEffect(MobEffects.DAMAGE_BOOST);
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, getAmplifierForEvo(player) + 2, false, false,false));
                        player.removeEffect(MobEffects.MOVEMENT_SPEED);
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 9, false, false, false));
                    }
                }
            }
        }
    }

    private static void removeEffects(ServerPlayer player) {
        Morph morph = MorphHolderAttacher.getCurrentMorphUnwrap(player);
        if (morph != null) {
            var effect = player.getEffect(MobEffects.MOVEMENT_SPEED);
            if (effect != null) {
                if (effect.getAmplifier() == 9) {
                    player.removeAllEffects();
                    morph.onMorphedTo(player);
                }
            }
        }
        MarkerHolderAttacher.getMarkerHolder(player).ifPresent(holder -> {
            if (holder.hasMarker(MarkerInit.UNBOUND_CONTROLS_MARKER.get())) {
                player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UnboundControlsPacket(true));
                holder.removeMarker(MarkerInit.UNBOUND_CONTROLS_MARKER.get(), false);
            }
        });
    }

    public static int getAmplifierForEvo(ServerPlayer player) {
        Morph morph = MorphHolderAttacher.getCurrentMorphUnwrap(player);
        if (morph == MorphInit.FREDDY_FAZBEAR.get()) {
            return 1;
        }
        else if (morph == MorphInit.GOLDEN_FREDDY_FAZBEAR.get()) {
            return 2;
        }
        else if (morph == MorphInit.NIGHTMARE_FREDDY_FAZBEAR.get()) {
            return 4;
        }
        return 0;
    }
}
