package dev._100media.rgrfreddy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev._100media.rgrfreddy.block.entity.JailDoorBE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class JailDoorBlockRenderer<T extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> {

    public JailDoorBlockRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void render(BlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (animatable instanceof JailDoorBE be) {
            if (be.getBlockState().hasProperty(DoorBlock.HALF)) {
                DoubleBlockHalf half = be.getBlockState().getValue(DoorBlock.HALF);
                if (half == DoubleBlockHalf.LOWER) {
                    poseStack.pushPose();
                    poseStack.mulPose(Axis.YP.rotationDegrees(90));
                    switch (be.getBlockState().getValue(DoorBlock.FACING)) {
                        case NORTH: poseStack.translate(0.15, 0, 0.5);
                        case SOUTH: poseStack.translate(0.4, 0, 0.3);
                        case EAST: poseStack.translate(0, 0, -0.8);
                        case WEST: poseStack.translate(-1, 0, 0.4);
                    }
                    super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
                    poseStack.popPose();
                }
            }
        }
    }
}
