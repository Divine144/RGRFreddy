package dev._100media.rgrfreddy.client.util;

import dev._100media.rgrfreddy.cap.FreddyHolder;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.util.ControllingPlayerCameraManager;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.Player;

public class ControlledPlayerUtil {
    public static boolean isSprinting;

    public static void customizeAiStep(RemotePlayer controlledPlayer) {
        if (ControllingPlayerCameraManager.controlledPlayer != controlledPlayer)
            return;

        Player controllingPlayer = FreddyHolderAttacher.getHolder(controlledPlayer).resolve().map(FreddyHolder::getControllingPlayer).orElse(null);

        if (!(controllingPlayer instanceof LocalPlayer localPlayer))
            return;

        // controlledPlayer.xxa = localPlayer.input.leftImpulse;
        // controlledPlayer.zza = localPlayer.input.forwardImpulse;
        // controlledPlayer.setJumping(localPlayer.input.jumping);
        //
        // Vec3 travelVec = new Vec3(controlledPlayer.xxa, controlledPlayer.yya, controlledPlayer.zza);
        //
        // controlledPlayer.travel(travelVec);

        // controlledPlayer.yBodyRot = controlledPlayer.getYRot();
        // controlledPlayer.yBodyRotO = controlledPlayer.yBodyRot;
        controlledPlayer.yHeadRot = controlledPlayer.getYRot();
        // controlledPlayer.yHeadRotO = controlledPlayer.yHeadRot;
        // controlledPlayer.setXRot(controllingPlayer.getXRot());
        // controlledPlayer.xRotO = controllingPlayer.xRotO;
        // controlledPlayer.setYRot(controllingPlayer.getYRot());
        // controlledPlayer.yRotO = controllingPlayer.yRotO;

        RemotePlayerExtension controlledExtension = (RemotePlayerExtension) controlledPlayer;

        int lerpSteps = controlledExtension.getLerpSteps();
        if (lerpSteps > 0) {
            double d0 = controlledPlayer.getX() + (controlledExtension.getLerpX() - controlledPlayer.getX()) / lerpSteps;
            double d1 = controlledPlayer.getY() + (controlledExtension.getLerpY() - controlledPlayer.getY()) / lerpSteps;
            double d2 = controlledPlayer.getZ() + (controlledExtension.getLerpZ() - controlledPlayer.getZ()) / lerpSteps;
            controlledExtension.setLerpSteps(lerpSteps - 1);
            controlledPlayer.setPos(d0, d1, d2);
            // controlledPlayer.setYRot(controlledPlayer.getYRot() + (float) Mth.wrapDegrees(controlledPlayer.lerpYRot - (double) controlledPlayer.getYRot()) / (float) controlledPlayer.lerpSteps);
            // controlledPlayer.setXRot(controlledPlayer.getXRot() + (float) (controlledPlayer.lerpXRot - (double) controlledPlayer.getXRot()) / (float) controlledPlayer.lerpSteps);
            // this.setRot(this.getYRot(), this.getXRot());
        }

        controlledExtension.setLerpSteps(0);
        controlledExtension.setLerpHeadSteps(0);
        // controlledExtension.setLerpDeltaMovementSteps(0);
    }

    public static void wrapSetSprinting(LocalPlayer instance, boolean isSprinting, BooleanConsumer operation) {
        operation.accept(isSprinting);

        if (ControllingPlayerCameraManager.controlledPlayer != null) {
            // operation.call(ControllingPlayerCameraManager.controlledPlayer, isSprinting);
            ControlledPlayerUtil.isSprinting = isSprinting;
        }
    }

    public static boolean isEffectiveAi(RemotePlayer controlledPlayer) {
        return false;
        // if (ControllingPlayerCameraManager.controlledPlayer != controlledPlayer)
        //     return false;
        //
        // Player controllingPlayer = FreddyHolderAttacher.getHolder(controlledPlayer).resolve().map(FreddyHolder::getControllingPlayer).orElse(null);
        //
        // return controllingPlayer instanceof LocalPlayer;
    }

    public static boolean wrapIsSprinting(LocalPlayer localPlayer, boolean sprinting) {
        if (ControllingPlayerCameraManager.controlledPlayer != null)
            return false;

        return sprinting;
    }
}
