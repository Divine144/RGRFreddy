package dev._100media.rgrfreddy.util;

import com.mojang.datafixers.util.Pair;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.entity.FreddyHatProjectileEntity;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.serverbound.LeaveControlPacket;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class ControllingPlayerCameraManager {

    public static RemotePlayer controlledPlayer;
    public static Entity previousCamera;
    private static CameraType previousCameraType;
    private static Pair<Float, Float> previousCameraView;

    @SubscribeEvent
    public static void tick(TickEvent.RenderTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || controlledPlayer == null || event.phase != TickEvent.Phase.START)
            return;

        if (controlledPlayer.isRemoved()) {
            if (minecraft.getCameraEntity() == controlledPlayer) {
                minecraft.setCameraEntity(previousCamera.isRemoved() ? minecraft.player : previousCamera);
                minecraft.options.setCameraType(previousCameraType);
            }
            previousCamera = null;
            remove();
            return;
        }

        if (previousCamera == null) {
            previousCameraType = minecraft.options.getCameraType();
            minecraft.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            previousCamera = minecraft.getCameraEntity();
            minecraft.setCameraEntity(controlledPlayer);
        }
    }

    public static void add(RemotePlayer proj) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        var holder = FreddyHolderAttacher.getHolderUnwrap(proj);
        if (player == null || holder == null || holder.getControllingPlayer() != player) {
            return;
        }
        controlledPlayer = proj;
        NetworkHandler.INSTANCE.sendToServer(new LeaveControlPacket(false));
        if (previousCameraView == null) {
            previousCameraView = new Pair<>(player.getXRot(), player.getYRot());
        }
    }

    public static void remove() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getCameraEntity() == controlledPlayer) {
            minecraft.setCameraEntity(previousCamera.isRemoved() ? minecraft.player : previousCamera);
            minecraft.options.setCameraType(previousCameraType);
        }
        NetworkHandler.INSTANCE.sendToServer(new LeaveControlPacket(true));
        previousCamera = null;
        controlledPlayer = null;
    }
}
