package dev._100media.rgrfreddy.network;

import com.mojang.blaze3d.platform.InputConstants;
import dev._100media.rgrfreddy.client.gui.JumpscareOverlay;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.mixin.MinecraftAccessor;
import dev._100media.rgrfreddy.util.ControllingPlayerCameraManager;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ClientHandler {

    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void startJumpscareAnimation(int evolutionStage) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            SoundEvent sound = switch (evolutionStage) {
                case 1 -> SoundInit.JUMP_TWO.get();
                case 2 -> SoundInit.JUMP_THREE.get();
                case 3 -> SoundInit.JUMP_FOUR.get();
                case 4 -> SoundInit.JUMP_FIVE.get();
                default -> SoundInit.JUMP_ONE.get();
            };
            player.level().playSound(player, player.blockPosition(), sound, SoundSource.PLAYERS, 0.65f, 1f);
        }
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

    public static void syncPlayerInputToControlled(boolean up, boolean down, boolean left, boolean right, boolean jump, boolean shift, float leftImpulse, float forwardImpulse) {
        Player currentPlayer = getPlayer();
        if (currentPlayer instanceof LocalPlayer controlled) {
            Input input = controlled.input;
            input.up = up;
            input.down = down;
            input.left = left;
            input.right = right;
            input.jumping = jump;
            input.shiftKeyDown = shift;
            input.forwardImpulse = forwardImpulse;
            input.leftImpulse = leftImpulse;
        }
    }

    public static void syncPlayerMouseControlled(float xRot, float yRot) {
        Player currentPlayer = getPlayer();
        if (currentPlayer instanceof LocalPlayer controlled) {
           controlled.turn(xRot, yRot);
        }
    }

    public static void addControlledPlayer() {
        Player currentPlayer = getPlayer();
        if (currentPlayer != null) {
            Player controlledPlayer = FreddyUtils.getControlledPlayer(currentPlayer);
            if (controlledPlayer != null) {
                ControllingPlayerCameraManager.add(controlledPlayer);
            }
        }
    }

    public static void handleClick() {
        Player currentPlayer = getPlayer();
        if (currentPlayer instanceof LocalPlayer controlled) {
            controlled.swing(InteractionHand.MAIN_HAND);
            if (Minecraft.getInstance() instanceof MinecraftAccessor accessor) {
                accessor.invokeStartAttack();
            }
        }
    }

    public static void addCamera(double x, double y, double z) {
        Player currentPlayer = getPlayer();
        if (currentPlayer instanceof LocalPlayer player) {
            Player controlledPlayer = FreddyUtils.getControlledPlayer(currentPlayer);
            if (controlledPlayer != null) {

            }
        }
    }
}
