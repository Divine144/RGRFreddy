package dev._100media.rgrfreddy.network;

import com.mojang.blaze3d.platform.InputConstants;
import dev._100media.rgrfreddy.client.gui.JumpscareOverlay;
import dev._100media.rgrfreddy.client.sound.HeartbeatSound;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.util.ControllingPlayerCameraManager;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ClientHandler {
    private static List<InputConstants.Key> originalKeys = List.of();

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
        if (originalKeys.isEmpty())
            originalKeys = getControlKeys().map(KeyMapping::getKey).toList();
        List<InputConstants.Key> scrambledKeys = new ArrayList<>(originalKeys);
        Collections.shuffle(scrambledKeys);
        // Apply scrambled to original
        List<KeyMapping> keyMappings = getControlKeys().toList();
        for (int i = 0; i < keyMappings.size(); i++) {
            keyMappings.get(i).setKey(scrambledKeys.get(i));
        }
        KeyMapping.resetMapping();
    }

    private static Stream<KeyMapping> getControlKeys() {
        Options options = Minecraft.getInstance().options;
        return Stream.of(options.keyJump, options.keyUp, options.keyDown, options.keyLeft, options.keyRight, options.keyShift, options.keySprint, options.keyAttack);
    }

    public static void resetAttack() {
        if (originalKeys.isEmpty()) {
            // We are not scrambled right now; nothing to do
            return;
        }

        // Apply original to scrambled
        List<KeyMapping> keyMappings = getControlKeys().toList();
        for (int i = 0; i < keyMappings.size(); i++) {
            keyMappings.get(i).setKey(originalKeys.get(i));
        }
        originalKeys = List.of();
        KeyMapping.resetMapping();
    }

    public static void syncPlayerInputToControlled(boolean up, boolean down, boolean left, boolean right, boolean jump, boolean shift, float leftImpulse, float forwardImpulse, boolean sprint) {
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
            controlled.setSprinting(sprint);
        }
    }

    public static void syncPlayerMouseControlled(float yRot, float xRot) {
        Player currentPlayer = getPlayer();
        if (currentPlayer instanceof LocalPlayer controlled) {
            controlled.turn(yRot, xRot);
        }
    }

    public static void addControlledPlayer() {
        Player currentPlayer = getPlayer();
        if (currentPlayer != null) {
            Player controlledPlayer = FreddyUtils.getControlledPlayer(currentPlayer);
            if (controlledPlayer != null) {
                ControllingPlayerCameraManager.add((RemotePlayer) controlledPlayer);
            }
        }
    }

    public static void handleClick() {
        Player currentPlayer = getPlayer();
        Minecraft minecraft = Minecraft.getInstance();
        if (currentPlayer instanceof LocalPlayer controlled) {
            if (minecraft.hitResult instanceof EntityHitResult result) {
                if (minecraft.gameMode != null) {
                    controlled.swing(InteractionHand.MAIN_HAND);
                    minecraft.gameMode.attack(controlled, result.getEntity());
                }
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

    public static void startHeartbeatSound(int entityId) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null)
            return;

        if (mc.level.getEntity(entityId) instanceof Player player)
            mc.getSoundManager().play(new HeartbeatSound(player));
    }
}
