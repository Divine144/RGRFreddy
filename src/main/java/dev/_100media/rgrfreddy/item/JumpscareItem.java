package dev._100media.rgrfreddy.item;

import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.init.MorphInit;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.PlayJumpscarePacket;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class JumpscareItem extends Item {

    public JumpscareItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.consume(itemStack);
        }
        if (MorphHolderAttacher.getCurrentMorph(pPlayer).isPresent()) {
            if (pPlayer instanceof ServerPlayer player) {
                var list = FreddyUtils.getEntitiesInRange(pPlayer, ServerPlayer.class, 10, 10, 10, p -> p != pPlayer);
                if (!list.isEmpty()) {
                    ServerPlayer targeted = list.get(0);
                    targeted.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 10, 0, false, false, false));
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> targeted), new PlayJumpscarePacket(getEvolutionStage(player)));
                    player.getCooldowns().addCooldown(this, 20 * 10);
                }
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    public int getEvolutionStage(ServerPlayer player) {
        var morph = MorphHolderAttacher.getCurrentMorphUnwrap(player);
        if (morph != null) {
            return List.of(MorphInit.KID_FREDDY.get(), MorphInit.TOY_FREDDY.get(), MorphInit.FREDDY_FAZBEAR.get(),
                    MorphInit.GOLDEN_FREDDY_FAZBEAR.get(), MorphInit.NIGHTMARE_FREDDY_FAZBEAR.get()).indexOf(morph);
        }
        return 0;
    }
}
