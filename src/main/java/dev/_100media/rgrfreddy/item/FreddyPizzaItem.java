package dev._100media.rgrfreddy.item;

import dev._100media.rgrfreddy.init.EntityInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class FreddyPizzaItem extends Item {

    public FreddyPizzaItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.pass(itemStack);

        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (!pLevel.isClientSide) {
            if (pLivingEntity instanceof ServerPlayer player) {
                if (pRemainingUseDuration % 2 == 0) {
                    var entity = EntityInit.PIZZA.get().create(pLevel);
                    if (entity != null) {
                        entity.setPos(player.getX(), player.getEyeY() - 0.55, player.getZ());
                        entity.setOwner(player);
                        entity.setNoGravity(true);
                        entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
                        entity.setYRot(-Mth.wrapDegrees(player.getYRot()));
                        entity.setXRot(-Mth.wrapDegrees(player.getXRot()));
                        entity.xRotO = -Mth.wrapDegrees(player.xRotO);
                        entity.yRotO = -Mth.wrapDegrees(player.yRotO);
                        player.level().addFreshEntity(entity);
                    }
                }
            }
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}
