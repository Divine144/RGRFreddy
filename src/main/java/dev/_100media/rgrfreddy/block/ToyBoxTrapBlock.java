package dev._100media.rgrfreddy.block;

import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.block.entity.ToyBoxTrapBE;
import dev._100media.rgrfreddy.cap.GlobalHolderAttacher;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.EffectInit;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ToyBoxTrapBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public ToyBoxTrapBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof ServerPlayer player && player.level() instanceof ServerLevel) {
            if (MorphHolderAttacher.getCurrentMorph(player).isEmpty() && pLevel.getBlockEntity(pPos) instanceof ToyBoxTrapBE be) {
                if (be.getTrappedPlayerUUID() == null) {
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 20, 0, false, false, false));
                    player.addEffect(new MobEffectInstance(EffectInit.NETTED.get(), 20 * 20, 0, false, false, false));
                    be.setTrappedPlayerUUID(player.getUUID());
                }
            }
        }
    }

    @Override
    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        if (!pLevel.isClientSide && pProjectile instanceof AbstractArrow) {
            if (pLevel.getBlockEntity(pHit.getBlockPos()) instanceof ToyBoxTrapBE be) {
                be.incrementTimesHit();
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
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockInit.TOY_BOX_TRAP_BE.get(), this::tick) : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ToyBoxTrapBE(pPos, pState);
    }

    private void tick(Level level, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof ToyBoxTrapBE toyBoxTrapBE) {
                if (toyBoxTrapBE.getTrappedPlayerUUID() != null) {
                    Player player = level.getPlayerByUUID(toyBoxTrapBE.getTrappedPlayerUUID());
                    if (player instanceof ServerPlayer serverPlayer) {
                        if (toyBoxTrapBE.getTimesHit() == 3) {
                            serverPlayer.removeEffect(EffectInit.NETTED.get());
                            serverPlayer.removeEffect(MobEffects.BLINDNESS);
                            level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                            return;
                        }
                        if (toyBoxTrapBE.getTimer() % 20 == 0) {
                            int seconds = toyBoxTrapBE.getTimer() / 20;
                            serverPlayer.connection.send(new ClientboundSetTitleTextPacket(Component.literal("%d".formatted(seconds)).withStyle(ChatFormatting.RED)));
                        }
                        if (toyBoxTrapBE.getTimer() == 0) {
                            GlobalHolderAttacher.getGlobalLevelCapability(level).ifPresent(holder -> {
                                BlockPos pos = holder.getToyBoxTeleportPos();
                                serverPlayer.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                            });
                            level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                            return;
                        }
                        toyBoxTrapBE.decrementTimer();
                    }
                }
                else if (toyBoxTrapBE.getTimesHit() >= 3) {
                    level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                }
                else {
                    var list = FreddyUtils.getEntitiesInRange(blockPos, level, Player.class, 10, 10, 10, p -> MorphHolderAttacher.getCurrentMorph(p).isEmpty());
                    list.forEach(player -> FreddyUtils.pullEntityToPoint(player, blockPos.getCenter(), 3));
                }
            }
        }
    }
}
