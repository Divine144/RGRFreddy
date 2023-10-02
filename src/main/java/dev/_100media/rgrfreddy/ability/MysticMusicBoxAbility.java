package dev._100media.rgrfreddy.ability;

import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.init.BlockInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MysticMusicBoxAbility extends Ability {

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        level.setBlockAndUpdate(player.blockPosition(), BlockInit.MYSTIC_MUSIC_BOX_BLOCK.get().defaultBlockState());
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
