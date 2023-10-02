package dev._100media.rgrfreddy.network;

import com.mojang.blaze3d.platform.InputConstants;
import dev._100media.rgrfreddy.client.gui.JumpscareOverlay;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public class ClientHandler {

    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void startJumpscareAnimation(int evolutionStage) {
        JumpscareOverlay.INSTANCE.setStartTime(Util.getMillis());
        JumpscareOverlay.INSTANCE.setEvolutionStage(evolutionStage);
        JumpscareOverlay.INSTANCE.setEnabled(true);
    }

    public static void unboundControls() {
        Minecraft mc = Minecraft.getInstance();
        mc.options.setKey(mc.options.keyJump, InputConstants.UNKNOWN);
        mc.options.setKey(mc.options.keyUp, InputConstants.UNKNOWN);
        mc.options.setKey(mc.options.keyDown, InputConstants.UNKNOWN);
        mc.options.setKey(mc.options.keyLeft, InputConstants.UNKNOWN);
        mc.options.setKey(mc.options.keyRight, InputConstants.UNKNOWN);
        mc.options.setKey(mc.options.keyShift, InputConstants.UNKNOWN);
        mc.options.setKey(mc.options.keySprint, InputConstants.UNKNOWN);
        KeyMapping.resetMapping();
    }

    public static void resetAttack() {
        Minecraft mc = Minecraft.getInstance();
        mc.options.keyAttack.setToDefault();
        mc.options.keyUp.setToDefault();
        mc.options.keyDown.setToDefault();
        mc.options.keyLeft.setToDefault();
        mc.options.keyRight.setToDefault();
        mc.options.keyJump.setToDefault();
        mc.options.keyShift.setToDefault();
        KeyMapping.resetMapping();
    }

    public static void syncPlayerInputToControlled(boolean up, boolean down, boolean left, boolean right, boolean shift, float forwardImpulse, float leftImpulse) {
        Player currentPlayer = getPlayer();
        if (currentPlayer instanceof LocalPlayer controlled) {
            Input input = controlled.input;
            input.up = up;
            input.down = down;
            input.left = left;
            input.right = right;
            input.shiftKeyDown = shift;
            input.forwardImpulse = forwardImpulse;
            input.leftImpulse = leftImpulse;
        }
    }
}
