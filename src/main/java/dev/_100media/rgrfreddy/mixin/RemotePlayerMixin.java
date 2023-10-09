package dev._100media.rgrfreddy.mixin;

import com.mojang.authlib.GameProfile;
import dev._100media.rgrfreddy.client.util.ControlledPlayerUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RemotePlayer.class)
public class RemotePlayerMixin extends AbstractClientPlayer {
    private RemotePlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "aiStep", at = @At("RETURN"))
    private void rgrfreddy$onAiStepReturn(CallbackInfo ci) {
        ControlledPlayerUtil.customizeAiStep((RemotePlayer) (Object) this);
    }

    @Override
    public boolean isEffectiveAi() {
        if (ControlledPlayerUtil.isEffectiveAi((RemotePlayer) (Object) this))
            return true;

        return super.isEffectiveAi();
    }
}
