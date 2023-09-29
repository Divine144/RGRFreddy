package dev._100media.rgrfreddy.item;

import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.init.EntityInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ToyArmyItem extends Item {
    public ToyArmyItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.consume(itemStack);
        }
        if (MorphHolderAttacher.getCurrentMorph(pPlayer).isPresent()) {
            for (int i = 0; i < 10; i++) {
                var entity = EntityInit.TOY_FREDDY.get().create(pLevel);
                if (entity != null) {
                    entity.setPos(pPlayer.position());
                    entity.setOwnerUUID(pPlayer.getUUID());
                    pLevel.addFreshEntity(entity);
                }
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }
}