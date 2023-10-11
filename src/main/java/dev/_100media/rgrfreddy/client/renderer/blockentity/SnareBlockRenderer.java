package dev._100media.rgrfreddy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev._100media.rgrfreddy.block.SnareBlock;
import dev._100media.rgrfreddy.block.entity.SnareBE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.SlabType;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SnareBlockRenderer<T extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> {

    public SnareBlockRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void render(BlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (animatable instanceof SnareBE be) {
            if (be.getBlockState().hasProperty(SnareBlock.TYPE)) {
                poseStack.pushPose();
                if (be.getBlockState().getValue(SnareBlock.TYPE) == SlabType.TOP) {
                    poseStack.translate(0, 0.5, 0);
                    super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
                }
                poseStack.popPose();
            }
        }
    }
}
