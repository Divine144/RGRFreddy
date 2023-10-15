package dev._100media.rgrfreddy.client.sound;

import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import dev._100media.rgrfreddy.init.SoundInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class HeartbeatSound extends AbstractTickableSoundInstance {
    private int tickCount;

    public HeartbeatSound() {
        super(SoundInit.HEARTBEAT.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.volume = 0.0F;
        this.pitch = 1.0F;
        this.delay = 0;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || MorphHolderAttacher.getCurrentMorphUnwrap(mc.player) == null) {
            this.stop();
            return;
        }

        // Not sure why mojang casts it to float and then back to double, but let's follow them!
        this.x = (float) mc.player.getX();
        this.y = (float) mc.player.getY();
        this.z = (float) mc.player.getZ();

        if (mc.player.isRemoved()) {
            // Keep sound playing at 0 volume so that it works when the player respawns
            this.volume = 0.0F;
            return;
        }

        if (this.tickCount % 20 == 0) {
            int maxDistance = 30;
            Player nearestPlayer = mc.level.getNearestPlayer(this.x, this.y, this.z, maxDistance, e -> e != mc.player && !e.isSpectator());
            if (nearestPlayer != null) {
                // Set volume higher if hunter is closer
                double distance = mc.player.distanceTo(nearestPlayer);
                this.volume = (float) (1.0F - Math.min(1.0F, distance / maxDistance));
            } else {
                // Keep sound running but at 0 volume
                this.volume = 0.0F;
            }
        }

        this.tickCount++;
    }
}
