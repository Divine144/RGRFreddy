package dev._100media.rgrfreddy.mixin;

import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.entity.FreddyHatProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Inject(method = "isControlledCamera", at = @At("HEAD"), cancellable = true)
    public void isControlledCamera(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer instance = (LocalPlayer) (Object) this;
        if (this.minecraft.getCameraEntity() instanceof FreddyHatProjectileEntity projectile && projectile.getOwner() == instance) {
            cir.setReturnValue(true);
        }
        else if (this.minecraft.getCameraEntity() instanceof RemotePlayer remotePlayer) {
            var holder = FreddyHolderAttacher.getHolderUnwrap(instance);
            if (holder != null) {
                UUID controllerUUID = holder.getControlledPlayer();
                if (controllerUUID != null && remotePlayer.getUUID().equals(controllerUUID)) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
