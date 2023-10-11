package dev._100media.rgrfreddy.util;

import com.mojang.datafixers.util.Pair;
import dev._100media.rgrfreddy.entity.FreddyHatProjectileEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class FreddyHatCameraManager {

    public static FreddyHatProjectileEntity projectile;
    private static Entity previousCamera;
    private static CameraType previousCameraType;
    private static Pair<Float, Float> previousCameraView;

    @SubscribeEvent
    public static void tick(TickEvent.RenderTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        if (projectile != null && projectile.isRemoved()) {
            remove();
            return;
        }
        if (projectile != null && previousCamera == null && event.phase == TickEvent.Phase.START) {
            previousCameraType = minecraft.options.getCameraType();
            minecraft.options.setCameraType(CameraType.FIRST_PERSON);
            previousCamera = minecraft.getCameraEntity();
            minecraft.setCameraEntity(projectile);
        }
        else if (projectile == null && previousCamera != null && event.phase == TickEvent.Phase.END) {
            minecraft.setCameraEntity(previousCamera);
            minecraft.options.setCameraType(previousCameraType);
            previousCamera = null;
        }
    }

    public static void add(FreddyHatProjectileEntity proj) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.player != proj.getOwner()) {
            return;
        }
        projectile = proj;
        if (previousCameraView == null) {
            previousCameraView = new Pair<>(minecraft.player.getXRot(), minecraft.player.getYRot());
        }
    }

    public static void remove() {
        Minecraft minecraft = Minecraft.getInstance();
        if (projectile == null || minecraft.player == null || minecraft.player != projectile.getOwner()) {
            return;
        }
        projectile = null;
    }
}
