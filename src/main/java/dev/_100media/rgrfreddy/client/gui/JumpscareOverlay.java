package dev._100media.rgrfreddy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev._100media.rgrfreddy.RGRFreddy;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;

public class JumpscareOverlay implements IGuiOverlay {

    public static final JumpscareOverlay INSTANCE = new JumpscareOverlay();
    private static final ResourceLocation[] EVO_1_FRAMES = initializeEvoOneFrames();
    private static final ResourceLocation[] EVO_2_FRAMES = initializeEvoTwoFrames();
    private static final ResourceLocation[] EVO_3_FRAMES = initializeEvoThreeFrames();
    private static final ResourceLocation[] EVO_4_FRAMES = initializeEvoFourFrames();
    private static final ResourceLocation[] EVO_5_FRAMES = initializeEvoFiveFrames();
    private static final List<ResourceLocation[]> framesList = List.of(EVO_1_FRAMES, EVO_2_FRAMES, EVO_3_FRAMES, EVO_4_FRAMES, EVO_5_FRAMES);

    private long startTime = 0;
    private boolean enabled = false;
    private int evolutionStage = 0;

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEvolutionStage(int evolutionStage) {
        this.evolutionStage = Mth.clamp(evolutionStage, 0, 4);
    }

    public boolean isEnabled() {
        return enabled;
    }

    private JumpscareOverlay() {}

    private static ResourceLocation[] initializeEvoOneFrames() {
        List<ResourceLocation> locations = new ArrayList<>();
        for (int i = 1; i <= 46; i++) {
            ResourceLocation location = new ResourceLocation(RGRFreddy.MODID, "textures/gui/evo_one/%04d.png".formatted(i));
            locations.add(location);
        }
        return locations.toArray(ResourceLocation[]::new);
    }

    private static ResourceLocation[] initializeEvoTwoFrames() {
        List<ResourceLocation> locations = new ArrayList<>();
        for (int i = 1; i <= 138; i++) {
            ResourceLocation location = new ResourceLocation(RGRFreddy.MODID, "textures/gui/evo_two/%04d.png".formatted(i));
            locations.add(location);
        }
        return locations.toArray(ResourceLocation[]::new);
    }

    private static ResourceLocation[] initializeEvoThreeFrames() {
        List<ResourceLocation> locations = new ArrayList<>();
        for (int i = 1; i <= 28; i++) {
            ResourceLocation location = new ResourceLocation(RGRFreddy.MODID, "textures/gui/evo_three/%04d.png".formatted(i));
            locations.add(location);
        }
        return locations.toArray(ResourceLocation[]::new);
    }

    private static ResourceLocation[] initializeEvoFourFrames() {
        List<ResourceLocation> locations = new ArrayList<>();
        for (int i = 1; i <= 111; i++) {
            ResourceLocation location = new ResourceLocation(RGRFreddy.MODID, "textures/gui/evo_four/%04d.png".formatted(i));
            locations.add(location);
        }
        return locations.toArray(ResourceLocation[]::new);
    }

    private static ResourceLocation[] initializeEvoFiveFrames() {
        List<ResourceLocation> locations = new ArrayList<>();
        for (int i = 1; i <= 28; i++) {
            ResourceLocation location = new ResourceLocation(RGRFreddy.MODID, "textures/gui/evo_five/%04d.png".formatted(i));
            locations.add(location);
        }
        return locations.toArray(ResourceLocation[]::new);
    }

    private ResourceLocation[] getFramesForEvo() {
        return framesList.get(evolutionStage);
    }

    private long getDurationForEvo() {
        return switch (evolutionStage) {
            case 0 -> 5000L;
            case 1 -> 8000L;
            default -> 10000L;
        };
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (!this.enabled)
            return;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        long elapsedTime = Util.getMillis() - this.startTime;
        ResourceLocation[] frames = getFramesForEvo();
        if (elapsedTime > getDurationForEvo()) {
            this.enabled = false;
            return;
        }
        int frameLength = frames.length - 1;
        int currentFrame = Mth.clamp((int) (elapsedTime / 20), 0, frameLength);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, ((float) ((frameLength) - currentFrame) / frameLength));
        RenderSystem.setShaderTexture(0, frames[currentFrame]);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0D, screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
    }
}
