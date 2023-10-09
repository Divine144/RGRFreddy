package dev._100media.rgrfreddy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.Blaze3D;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.serverbound.NotifyServerMousePacket;
import dev._100media.rgrfreddy.util.ControllingPlayerCameraManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.SmoothDouble;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    private void rgrFreddy$injectTurnPlayer(CallbackInfo ci) {
        LocalPlayer instance = Minecraft.getInstance().player;
        if (instance == null)
            return;

        var holder = FreddyHolderAttacher.getHolderUnwrap(instance);
        if (holder == null)
            return;

        if (holder.getControllingPlayer() != null)
            ci.cancel();
    }

    @WrapOperation(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private void rgrFreddy$wrapTurnPlayer_Turn(double yRot, double xRot, Operation<LocalPlayer> operation) {
        if (ControllingPlayerCameraManager.controlledPlayer == null) {
            operation.call(this.minecraft.player, yRot, xRot);
        } else {
            
        }
    }
}
