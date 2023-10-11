package dev._100media.rgrfreddy.util;

import com.mojang.datafixers.util.Pair;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class ControllingPlayerCameraManager {

    public static Player controlledPlayer;
    public static Entity previousCamera;
    private static CameraType previousCameraType;
    private static Pair<Float, Float> previousCameraView;

    @SubscribeEvent
    public static void tick(TickEvent.RenderTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        if (controlledPlayer != null && controlledPlayer.isRemoved() || controlledPlayer != null && controlledPlayer.isDeadOrDying()) {
            remove();
            return;
        }
        if (controlledPlayer != null && previousCamera == null && event.phase == TickEvent.Phase.START) {
            previousCameraType = minecraft.options.getCameraType();
            minecraft.options.setCameraType(CameraType.FIRST_PERSON);
            previousCamera = minecraft.getCameraEntity();
            minecraft.setCameraEntity(controlledPlayer);
        }
        else if (controlledPlayer == null && previousCamera != null && event.phase == TickEvent.Phase.END) {
            minecraft.setCameraEntity(previousCamera);
            minecraft.options.setCameraType(previousCameraType);
            previousCamera = null;
        }
    }

    public static void add(Player proj) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        var holder = FreddyHolderAttacher.getHolderUnwrap(proj);
        if (player == null || holder == null || holder.getControllingPlayer() == null || !player.getUUID().equals(holder.getControllingPlayer())) {
            return;
        }
        controlledPlayer = proj;
        if (previousCameraView == null) {
            previousCameraView = new Pair<>(player.getXRot(), player.getYRot());
        }
    }

    public static void remove() {
        Minecraft minecraft = Minecraft.getInstance();
        if (controlledPlayer == null || minecraft.player == null) {
            return;
        }
        controlledPlayer = null;
    }
}
