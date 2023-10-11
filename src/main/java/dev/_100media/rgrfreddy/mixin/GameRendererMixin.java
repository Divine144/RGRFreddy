package dev._100media.rgrfreddy.mixin;

import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.util.ControllingPlayerCameraManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    Minecraft minecraft;

    @Redirect(
            method = "renderItemInHand",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;getPlayerMode()Lnet/minecraft/world/level/GameType;"
            )
    )
    public GameType renderItemInHand(MultiPlayerGameMode instance) {
        LocalPlayer player = this.minecraft.player;
        if (player != null) {
            var holder = FreddyHolderAttacher.getHolderUnwrap(player);
            if (holder != null && holder.getControlledPlayer() != null && ControllingPlayerCameraManager.controlledPlayer != null) {
                return GameType.SPECTATOR; // Forces hands to not render
            }
        }
        return instance.getPlayerMode();
    }
}
