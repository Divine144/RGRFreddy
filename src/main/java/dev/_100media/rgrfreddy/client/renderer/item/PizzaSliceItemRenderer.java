package dev._100media.rgrfreddy.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PizzaSliceItemRenderer<T extends Entity & ItemSupplier> extends EntityRenderer<T> {
   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
   private final ItemRenderer itemRenderer;
   private final float scale;
   private final boolean fullBright;

   public PizzaSliceItemRenderer(EntityRendererProvider.Context pContext, float pScale, boolean pFullBright) {
      super(pContext);
      this.itemRenderer = pContext.getItemRenderer();
      this.scale = pScale;
      this.fullBright = pFullBright;
   }

   public PizzaSliceItemRenderer(EntityRendererProvider.Context pContext) {
      this(pContext, 1.0F, false);
   }

   protected int getBlockLightLevel(T pEntity, BlockPos pPos) {
      return this.fullBright ? 15 : super.getBlockLightLevel(pEntity, pPos);
   }

   public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
      if (pEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pEntity) < 12.25D)) {
         pPoseStack.pushPose();
         pPoseStack.scale(this.scale, this.scale, this.scale);
         pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) + 180.0F));
         // pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
         this.itemRenderer.renderStatic(pEntity.getItem(), ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pEntity.level(), pEntity.getId());
         pPoseStack.popPose();
         super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
      }
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getTextureLocation(Entity pEntity) {
      return TextureAtlas.LOCATION_BLOCKS;
   }
}