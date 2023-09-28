package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.hundredmediaabilities.HundredMediaAbilitiesMod;
import dev._100media.hundredmediaabilities.marker.Marker;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;

public class MarkerInit {
    public static final DeferredRegister<Marker> MARKERS = DeferredRegister.create(new ResourceLocation(HundredMediaAbilitiesMod.MODID, "markers"), RGRFreddy.MODID);
/*    public static final RegistryObject<Marker> MECHA_BOARD_MARKER = MARKERS.register("mecha_board", MechaBoardMarker::new);*/
}
