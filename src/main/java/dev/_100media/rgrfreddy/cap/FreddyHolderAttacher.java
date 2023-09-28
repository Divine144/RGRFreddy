package dev._100media.rgrfreddy.cap;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RGRFreddy.MODID)
public class FreddyHolderAttacher extends CapabilityAttacher {
    public static final Capability<FreddyHolder> CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation LOCATION = new ResourceLocation(RGRFreddy.MODID, "example");
    private static final Class<FreddyHolder> CAPABILITY_CLASS = FreddyHolder.class;

    @SuppressWarnings("ConstantConditions")
    public static FreddyHolder getHolderUnwrap(Entity entity) {
        return getHolder(entity).orElse(null);
    }

    public static LazyOptional<FreddyHolder> getHolder(Entity entity) {
        return entity.getCapability(CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<Entity> event, Player entity) {
        genericAttachCapability(event, new FreddyHolder(entity), CAPABILITY, LOCATION);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerPlayerAttacher(FreddyHolderAttacher::attach, FreddyHolderAttacher::getHolder, true);
    }
}
