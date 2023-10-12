package dev._100media.rgrfreddy.mixin;

import com.mojang.authlib.GameProfile;
import dev._100media.rgrfreddy.client.util.ControlledPlayerUtil;
import dev._100media.rgrfreddy.client.util.RemotePlayerExtension;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RemotePlayer.class)
public class RemotePlayerMixin extends AbstractClientPlayer implements RemotePlayerExtension {
    @Shadow
    private int lerpDeltaMovementSteps;

    private RemotePlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void rgrfreddy$onAiStepHead(CallbackInfo ci) {
        ControlledPlayerUtil.customizeAiStep((RemotePlayer) (Object) this);
    }

    @Override
    public boolean isEffectiveAi() {
        if (ControlledPlayerUtil.isEffectiveAi((RemotePlayer) (Object) this))
            return true;

        return super.isEffectiveAi();
    }

    @Override
    public int getLerpSteps() {
        return this.lerpSteps;
    }

    @Override
    public void setLerpSteps(int lerpSteps) {
        this.lerpSteps = lerpSteps;
    }

    @Override
    public int getLerpHeadSteps() {
        return this.lerpHeadSteps;
    }

    @Override
    public void setLerpHeadSteps(int lerpHeadSteps) {
        this.lerpHeadSteps = lerpHeadSteps;
    }

    @Override
    public int getLerpDeltaMovementSteps() {
        return this.lerpDeltaMovementSteps;
    }

    @Override
    public void setLerpDeltaMovementSteps(int lerpDeltaMovementSteps) {
        this.lerpDeltaMovementSteps = lerpDeltaMovementSteps;
    }

    @Override
    public double getLerpX() {
        return this.lerpX;
    }

    @Override
    public double getLerpY() {
        return this.lerpY;
    }

    @Override
    public double getLerpZ() {
        return this.lerpZ;
    }
}
