package dev._100media.rgrfreddy.client.renderer;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.item.GuardMaskArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GuardMaskArmorRenderer extends GeoArmorRenderer<GuardMaskArmorItem> {

    public GuardMaskArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(RGRFreddy.MODID, "armor/guard_mask")));
    }
}
