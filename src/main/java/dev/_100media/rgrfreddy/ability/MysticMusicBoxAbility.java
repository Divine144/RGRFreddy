package dev._100media.rgrfreddy.ability;

import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.rgrfreddy.init.BlockInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class MysticMusicBoxAbility extends Ability {

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        var effect = player.getEffect(MobEffects.DAMAGE_BOOST);
        if (effect != null) {
            int amplifier = effect.getAmplifier();
            player.removeEffect(MobEffects.DAMAGE_BOOST);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, amplifier + 2, false, false, false));
            player.removeEffect(MobEffects.MOVEMENT_SPEED);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 9, false, false, false));
        }
        level.setBlockAndUpdate(player.blockPosition(), BlockInit.MYSTIC_MUSIC_BOX_BLOCK.get().defaultBlockState());
        super.executePressed(level, player);
    }
}
