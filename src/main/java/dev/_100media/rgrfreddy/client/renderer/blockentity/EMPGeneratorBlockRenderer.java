package dev._100media.rgrfreddy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev._100media.rgrfreddy.block.entity.EMPGeneratorBE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class EMPGeneratorBlockRenderer<T extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> {

    public EMPGeneratorBlockRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void render(BlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (animatable instanceof EMPGeneratorBE) {
            poseStack.pushPose();
            poseStack.translate(-0.5, 0, -0.5);
            super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
        }
    }
}
