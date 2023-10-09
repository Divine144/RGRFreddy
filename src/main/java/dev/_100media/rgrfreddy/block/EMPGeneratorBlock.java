package dev._100media.rgrfreddy.block;

import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.block.entity.EMPGeneratorBE;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EMPGeneratorBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public EMPGeneratorBlock(Properties pProperties) {
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
        return Shapes.block();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockInit.EMP_GENERATOR_BE.get(), this::tick) : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EMPGeneratorBE(pPos, pState);
    }

    private void tick(Level level, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.getServer().getTickCount() % 3 == 0) {
                serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 3, 0, 0, 0.2, 0.1);
            }
            var list = FreddyUtils.getEntitiesInRange(blockPos, level, Player.class, 10, 10, 10, p -> MorphHolderAttacher.getCurrentMorph(p).isPresent());
            if (!list.isEmpty()) {
                if (serverLevel.getServer().getTickCount() % 30 == 0) {
                    level.playSound(null, blockPos, SoundInit.GENERATOR.get(), SoundSource.PLAYERS, 0.65f, 1f);
                }
                Player player = list.get(0);
                var holder = FreddyHolderAttacher.getHolderUnwrap(player);
                if (holder != null && !holder.isAbilitiesDisabled()) {
                    holder.setAbilitiesDisabled(true);
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.sendSystemMessage(Component.literal("Your abilities have been disabled!").withStyle(ChatFormatting.RED), true);
                    }
                }
            }
            serverLevel.players().stream()
                    .filter(p -> MorphHolderAttacher.getCurrentMorph(p).isPresent())
                    .filter(player -> !list.contains(player)).forEach(player -> {
                var holder = FreddyHolderAttacher.getHolderUnwrap(player);
                if (holder != null && holder.isAbilitiesDisabled()) {
                    holder.setAbilitiesDisabled(false);
                    player.sendSystemMessage(Component.literal("Your abilities have been restored!").withStyle(ChatFormatting.GREEN), true);
                }
            });
        }
    }
}
