package dev._100media.rgrfreddy.ability;

import com.nyfaria.hmutility.utils.HMUVectorUtils;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.init.BlockInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;

public class FreddySnaresAbility extends Ability {

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        BlockHitResult result = HMUVectorUtils.blockTrace(player, ClipContext.Fluid.NONE, 10, false);
        if (result != null && !level.getBlockState(result.getBlockPos()).isAir()) {
            level.setBlockAndUpdate(result.getBlockPos(), BlockInit.SNARE_BLOCK.get().defaultBlockState());
        }
        super.executePressed(level, player);
    }

    @Override
    public boolean isOnCooldown(Player player) {
        var holder = FreddyHolderAttacher.getHolderUnwrap(player);
        if (holder != null && holder.isAbilitiesDisabled()) {
            return true;
        }
        return super.isOnCooldown(player);
    }
}
