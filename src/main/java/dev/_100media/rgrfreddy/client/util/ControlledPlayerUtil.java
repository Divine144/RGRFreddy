package dev._100media.rgrfreddy.client.util;

import dev._100media.rgrfreddy.cap.FreddyHolder;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.util.ControllingPlayerCameraManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ControlledPlayerUtil {
    public static void customizeAiStep(RemotePlayer controlledPlayer) {
        if (ControllingPlayerCameraManager.controlledPlayer != controlledPlayer)
            return;

        Player controllingPlayer = FreddyHolderAttacher.getHolder(controlledPlayer).resolve().map(FreddyHolder::getControllingPlayer).orElse(null);

        if (!(controllingPlayer instanceof LocalPlayer localPlayer))
            return;

        controlledPlayer.xxa = localPlayer.input.leftImpulse;
        controlledPlayer.zza = localPlayer.input.forwardImpulse;
        controlledPlayer.setJumping(localPlayer.input.jumping);

        Vec3 travelVec = new Vec3(controlledPlayer.xxa, controlledPlayer.yya, controlledPlayer.zza);

        controlledPlayer.travel(travelVec);

        controlledPlayer.yBodyRot = controllingPlayer.yBodyRot;
        controlledPlayer.yBodyRotO = controllingPlayer.yBodyRotO;
        controlledPlayer.yHeadRot = controllingPlayer.yHeadRot;
        controlledPlayer.yHeadRotO = controllingPlayer.yHeadRotO;
        controlledPlayer.setXRot(controllingPlayer.getXRot());
        controlledPlayer.xRotO = controllingPlayer.xRotO;
        controlledPlayer.setYRot(controllingPlayer.getYRot());
        controlledPlayer.yRotO = controllingPlayer.yRotO;

        // this.yBobO = this.yBob;
        // this.xBobO = this.xBob;
        // this.xBob += (this.getXRot() - this.xBob) * 0.5F;
        // this.yBob += (this.getYRot() - this.yBob) * 0.5F;
    }

    public static boolean isEffectiveAi(RemotePlayer controlledPlayer) {
        if (ControllingPlayerCameraManager.controlledPlayer != controlledPlayer)
            return false;

        Player controllingPlayer = FreddyHolderAttacher.getHolder(controlledPlayer).resolve().map(FreddyHolder::getControllingPlayer).orElse(null);

        return controllingPlayer instanceof LocalPlayer;
    }
}
