package dev._100media.rgrfreddy.mixin;

import dev._100media.rgrfreddy.cap.FreddyHolder;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow protected abstract void continueAttack(boolean pLeftClick);

    @Shadow @Nullable public LocalPlayer player;

    @Redirect(method = "handleKeybinds()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;continueAttack(Z)V"))
    public void handleKeyBinds(Minecraft instance, boolean direction) {
        LocalPlayer player = instance.player;
        if (player != null) {
            Player controllingPlayer = FreddyUtils.getControllingPlayer(player);
            if (controllingPlayer != null) {
                this.continueAttack(instance.options.keyAttack.isDown());
            }
            else this.continueAttack(direction);
        }
    }
}
