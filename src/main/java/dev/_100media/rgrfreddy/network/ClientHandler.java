package dev._100media.rgrfreddy.network;

import com.mojang.blaze3d.platform.InputConstants;
import dev._100media.rgrfreddy.client.gui.JumpscareOverlay;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class ClientHandler {

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
}
