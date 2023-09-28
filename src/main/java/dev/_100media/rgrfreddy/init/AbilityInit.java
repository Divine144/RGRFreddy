package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.hundredmediaabilities.HundredMediaAbilitiesMod;
import dev._100media.hundredmediaabilities.ability.Ability;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AbilityInit {

    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(new ResourceLocation(HundredMediaAbilitiesMod.MODID, "abilities"), RGRFreddy.MODID);

    public static final RegistryObject<Ability> LASER_TURRET_SHOOT = ABILITIES.register("laser_turret_shoot", Ability::new);

    public static final RegistryObject<Ability> LASER_TURRET_MORPH = ABILITIES.register("laser_turret_morph", Ability::new);

    public static final RegistryObject<Ability> TESLA_COIL = ABILITIES.register("tesla_coil", Ability::new);

    public static final RegistryObject<Ability> FUSION_CORE_REACTOR = ABILITIES.register("fusion_core_reactor", Ability::new);

    public static final RegistryObject<Ability> SCULKY_MECHA_MINES = ABILITIES.register("sculky_mecha_mines", Ability::new);
}
