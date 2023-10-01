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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SnareBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public SnareBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof ServerPlayer player && player.level() instanceof ServerLevel level) {
            if (MorphHolderAttacher.getCurrentMorph(player).isEmpty() && pLevel.getBlockEntity(pPos) instanceof SnareBE be) {
                if (be.getTrappedPlayerUUID() == null) {
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, -1, 0, false, false, false));
                    player.addEffect(new MobEffectInstance(EffectInit.NETTED.get(), -1, 0, false, false, false));
                    level.players().forEach(pl -> {
                        pl.sendSystemMessage(Component.literal(player.getDisplayName().getString() + " has been trapped. Location: %s %s %s"
                                .formatted(player.getX(), player.getY(), player.getZ())).withStyle(ChatFormatting.RED));
                    });
                    be.setTrappedPlayerUUID(player.getUUID());
                }
            }
        }
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
                        var list = FreddyUtils.getEntitiesInRange(blockPos, level, ServerPlayer.class, 20, 20, 20, p -> p != player && p.tickCount % 12 == 0);
                        list.forEach(pl -> pl.playNotifySound(SoundInit.HEARTBEAT.get(), SoundSource.PLAYERS, 0.65f, 1f));
                    }
                }
            }
        }
    }
}
