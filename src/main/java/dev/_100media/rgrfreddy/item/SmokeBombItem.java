package dev._100media.rgrfreddy.item;

import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.init.SoundInit;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SmokeBombItem extends Item {

    public SmokeBombItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.consume(itemStack);
        }
        pLevel.playSound(null, pPlayer.blockPosition(), SoundInit.SMOKE.get(), SoundSource.PLAYERS, 0.65f, 1f);
        pPlayer.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 30, 0, false, false, false));
        pLevel.setBlockAndUpdate(pPlayer.blockPosition(), BlockInit.SMOKE_BOMB_BLOCK.get().defaultBlockState());
        return InteractionResultHolder.pass(itemStack);
    }
}
