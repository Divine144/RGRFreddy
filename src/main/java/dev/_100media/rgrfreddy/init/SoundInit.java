package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RGRFreddy.MODID);

    public static final RegistryObject<SoundEvent> HEARTBEAT = sound("heartbeat");

    public static final RegistryObject<SoundEvent> MICROPHONE = sound("microphone");

    public static final RegistryObject<SoundEvent> MUSIC_BOX = sound("music_box");

/*    public static final RegistryObject<SoundEvent> SHOCK_TRAP = sound("shock_trap");
    public static final RegistryObject<SoundEvent> WRIST_ROCKETS = sound("wrist_rockets");
    public static final RegistryObject<SoundEvent> WARDEN_LASER = sound("warden_laser");
    public static final RegistryObject<SoundEvent> MECHO_LOCATION = sound("mecholocation");
    public static final RegistryObject<SoundEvent> FUSION_REACTOR = sound("fusion_reactor");
    public static final RegistryObject<SoundEvent> TESLA_COIL = sound("tesla_coil");
    public static final RegistryObject<SoundEvent> MECHA_MINES = sound("mecha_mines");
    public static final RegistryObject<SoundEvent> DEEP_DARK_DESTROYER = sound("deep_dark_destroyer");
    public static final RegistryObject<SoundEvent> LASER = sound("laser");
    public static final RegistryObject<SoundEvent> DEEP_DARK = sound("deep_dark");
    public static final RegistryObject<SoundEvent> RAY = sound("ray");*/


    private static RegistryObject<SoundEvent> sound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RGRFreddy.MODID, name)));
    }
}
