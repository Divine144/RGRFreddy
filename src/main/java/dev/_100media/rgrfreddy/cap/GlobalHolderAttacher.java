package dev._100media.rgrfreddy.cap;

import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import dev._100media.rgrfreddy.RGRFreddy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public class GlobalHolderAttacher extends CapabilityAttacher {

    private static final Class<GlobalHolder> CAPABILITY_CLASS = GlobalHolder.class;
    public static final Capability<GlobalHolder> EXAMPLE_GLOBAL_LEVEL_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation EXAMPLE_GLOBAL_LEVEL_CAPABILITY_RL = new ResourceLocation(RGRFreddy.MODID, "global_capability");

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static GlobalHolder getGlobalLevelCapabilityUnwrap(Level level) {
        return getGlobalLevelCapability(level).orElse(null);
    }

    public static LazyOptional<GlobalHolder> getGlobalLevelCapability(Level level) {
        return getGlobalLevelCapability(level, EXAMPLE_GLOBAL_LEVEL_CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<Level> event, Level level) {
        genericAttachCapability(event, new GlobalHolder(level), EXAMPLE_GLOBAL_LEVEL_CAPABILITY, EXAMPLE_GLOBAL_LEVEL_CAPABILITY_RL);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerGlobalLevelAttacher(GlobalHolderAttacher::attach, GlobalHolderAttacher::getGlobalLevelCapability);
    }
}
