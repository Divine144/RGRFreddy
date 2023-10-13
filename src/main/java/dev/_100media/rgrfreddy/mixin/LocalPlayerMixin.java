package dev._100media.rgrfreddy.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.client.util.ControlledPlayerUtil;
import dev._100media.rgrfreddy.entity.FreddyHatProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    private boolean lastOnGround;

    @Shadow
    public Input input;

    @Shadow protected abstract boolean hasEnoughFoodToStartSprinting();

    private LocalPlayerMixin(ClientLevel pClientLevel, GameProfile pGameProfile) {
        super(pClientLevel, pGameProfile);
    }

    @Inject(method = "isControlledCamera", at = @At("HEAD"), cancellable = true)
    public void isControlledCamera(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer instance = (LocalPlayer) (Object) this;
        if (this.minecraft.getCameraEntity() instanceof FreddyHatProjectileEntity projectile && projectile.getOwner() == instance) {
            cir.setReturnValue(true);
        }
/*        else if (this.minecraft.getCameraEntity() instanceof RemotePlayer remotePlayer) {
            var holder = FreddyHolderAttacher.getHolderUnwrap(instance);
            if (holder != null) {
                UUID controllerUUID = holder.getControlledPlayer();
                if (controllerUUID != null && remotePlayer.getUUID().equals(controllerUUID)) {
                    cir.setReturnValue(true);
                }
            }
        }*/
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(ZF)V"))
    public void tickInput(Input instance, boolean pIsSneaking, float pSneakingSpeedMultiplier) {
        var holder = FreddyHolderAttacher.getHolderUnwrap((LocalPlayer) (Object) this);

        if (holder != null) {
            Player controllingPlayer = holder.getControllingPlayer();

            // Only tick the input if the local player is not controlling a player
            if (controllingPlayer == null) {
                instance.tick(pIsSneaking, pSneakingSpeedMultiplier);
            }
        }

        if (super.isSprinting()) {
            boolean flag7 = !this.input.hasForwardImpulse() || !this.hasEnoughFoodToStartSprinting();
            boolean flag8 = flag7 || this.horizontalCollision && !this.minorHorizontalCollision || this.isInWater() && !this.isUnderWater()
                            || (this.isInFluidType((fluidType, height) -> this.canSwimInFluidType(fluidType)) && !this.canStartSwimming());
            if (this.isSwimming()) {
                if (!this.onGround() && !this.input.shiftKeyDown && flag7 || !(this.isInWater() || this.isInFluidType((fluidType, height) -> this.canSwimInFluidType(fluidType)))) {
                    this.setSprinting(false);
                }
            } else if (flag8) {
                this.setSprinting(false);
            }
        }
    }

    @Override
    public void setSprinting(boolean isSprinting) {
        ControlledPlayerUtil.wrapSetSprinting((LocalPlayer) (Object) this, isSprinting, super::setSprinting);
    }

    @Override
    public boolean isSprinting() {
        return ControlledPlayerUtil.wrapIsSprinting((LocalPlayer) (Object) this, super.isSprinting());
    }
}
