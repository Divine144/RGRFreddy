package dev._100media.rgrfreddy.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev._100media.rgrfreddy.block.DimensionalTrapDoorBlock;
import dev._100media.rgrfreddy.block.entity.DimensionalTrapDoorBE;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class DimensionalTrapDoorRenderer<T extends DimensionalTrapDoorBE> implements BlockEntityRenderer<T> {

    public DimensionalTrapDoorRenderer(BlockEntityRendererProvider.Context pContext) {}

    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        BlockState state = pBlockEntity.getBlockState();
        Matrix4f matrix4f = pPoseStack.last().pose();
        if (state.hasProperty(DimensionalTrapDoorBlock.OPEN)) {
            if (state.hasProperty(TrapDoorBlock.FACING)) {
                if (!state.getValue(DimensionalTrapDoorBlock.OPEN)) {
                    matrix4f.scale(0.85f, 1f, 1f);
                    matrix4f.translate(0, -0.32f,0);
                }
                else {
                    switch (state.getValue(TrapDoorBlock.FACING)) {
                        case EAST -> {
                            matrix4f.translate(0.15f, 0f, 0.01f);
                            matrix4f.scale(0.85f, 1f, 1f);
                        }
                        case SOUTH -> {
                            matrix4f.translate(0.01f, 0f, 0.15f);
                            matrix4f.scale(1f, 1f, 0.85f);
                        }
                        case NORTH -> {
                            matrix4f.translate(0.01f, 0f, 0f);
                            matrix4f.scale(1f, 1f, 0.85f);
                        }
                        case WEST -> {
                            matrix4f.translate(0f, 0f, 0.01f);
                            matrix4f.scale(0.85f, 1f, 1f);
                        }
                    }
                    matrix4f.translate(0, -0.47f,0);
                }
                this.renderCube(pBlockEntity, matrix4f, pBuffer.getBuffer(this.renderType()));
            }
        }
    }

    private void renderCube(T pBlockEntity, Matrix4f pPose, VertexConsumer pConsumer) {
        float f = this.getOffsetDown();
        float f1 = this.getOffsetUp();
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(pBlockEntity, pPose, pConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 1.0F, f1, f1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFace(T pBlockEntity, Matrix4f pPose, VertexConsumer pConsumer, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        if (pBlockEntity.shouldRenderFace(pDirection)) {
            pConsumer.vertex(pPose, pX0, pY0, pZ0).endVertex();
            pConsumer.vertex(pPose, pX1, pY0, pZ1).endVertex();
            pConsumer.vertex(pPose, pX1, pY1, pZ2).endVertex();
            pConsumer.vertex(pPose, pX0, pY1, pZ3).endVertex();
        }
    }

    protected float getOffsetUp() {
        return 0.5F;
    }

    protected float getOffsetDown() {
        return 0.05F;
    }

    protected RenderType renderType() {
        return RenderType.endPortal();
    }
}
