package dev._100media.rgrfreddy.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow protected abstract void continueAttack(boolean pLeftClick);

    @Shadow @Nullable public LocalPlayer player;
}
