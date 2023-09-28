package dev._100media.rgrfreddy.mixin;

import dev._100media.rgrfreddy.quest.goal.GiveParrotCookieGoal;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Parrot.class)
public class ParrotMixin {

    @Inject(
            method = "mobInteract",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/animal/Parrot;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
            )
    )
    public void rgrfreddy$injectMobInteract_AddEffect(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (pPlayer instanceof ServerPlayer player) {
            FreddyUtils.addToGenericQuestGoal(player, GiveParrotCookieGoal.class);
        }
    }
}
