package dev._100media.rgrfreddy.ability;

import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.hundredmediamorphs.morph.Morph;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.init.EffectInit;
import dev._100media.rgrfreddy.init.MorphInit;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class EerieLullabyAbility extends Ability {

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        var list = FreddyUtils.getEntitiesInRange(player, Player.class, 10, 10, 10, p -> p != player);
        int fearTicks = getFearTicksForEvo(player);
        list.forEach(p -> {
            FreddyHolderAttacher.getHolder(p).ifPresent(freddyHolder -> {
                freddyHolder.setFearTicks(fearTicks);
                p.addEffect(new MobEffectInstance(EffectInit.NETTED.get(), fearTicks, 0, false, false, false));
            });
        });
        level.playSound(null, player.blockPosition(), SoundInit.LULLABY.get(), SoundSource.PLAYERS, 0.6f, 1f);
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

    public int getFearTicksForEvo(ServerPlayer player) {
        Morph morph = MorphHolderAttacher.getCurrentMorphUnwrap(player);
        if (morph == MorphInit.KID_FREDDY.get()) {
            return 20 * 5;
        }
        else if (morph == MorphInit.TOY_FREDDY.get()) {
            return 20 * 7;
        }
        else if (morph == MorphInit.FREDDY_FAZBEAR.get()) {
            return 20 * 10;
        }
        else if (morph == MorphInit.GOLDEN_FREDDY_FAZBEAR.get()) {
            return 20 * 15;
        }
        return 20 * 30;
    }
}
