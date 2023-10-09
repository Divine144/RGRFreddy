package dev._100media.rgrfreddy.mixin;

import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.init.MorphInit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {Entity.class, Player.class})
public class MorphRidingOffsetMixin {

    @Inject(
            at = @At(value = "RETURN"),
            method = "getMyRidingOffset",
            cancellable = true
    )
    private void getMyRidingOffset(CallbackInfoReturnable<Double> cir) {
        if ((Object)this instanceof LivingEntity living) {
            MorphHolderAttacher.getMorphHolder(living).ifPresent(holder -> {
                if (holder.getCurrentMorph() != null) {
                    if (holder.getCurrentMorph() == MorphInit.KID_FREDDY.get()) {
                        cir.setReturnValue(0.25d);
                    }
                }
            });
        }
    }
}
