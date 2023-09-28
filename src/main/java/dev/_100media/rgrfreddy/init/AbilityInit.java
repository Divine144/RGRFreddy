package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.hundredmediaabilities.HundredMediaAbilitiesMod;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.rgrfreddy.ability.DimensionalTrapdoorAbility;
import dev._100media.rgrfreddy.ability.EerieLullabyAbility;
import dev._100media.rgrfreddy.ability.FreddySnaresAbility;
import dev._100media.rgrfreddy.ability.MysticMusicBoxAbility;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AbilityInit {

    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(new ResourceLocation(HundredMediaAbilitiesMod.MODID, "abilities"), RGRFreddy.MODID);

    public static final RegistryObject<Ability> EERIE_LULLABY = ABILITIES.register("eerie_lullaby", EerieLullabyAbility::new);

    public static final RegistryObject<Ability> MYSTIC_MUSIC_BOX = ABILITIES.register("mystic_music_box", MysticMusicBoxAbility::new);

    public static final RegistryObject<Ability> FREDDY_SNARES = ABILITIES.register("freddy_snares", FreddySnaresAbility::new);

    public static final RegistryObject<Ability> DIMENSIONAL_TRAPDOOR = ABILITIES.register("dimensional_trapdoor", DimensionalTrapdoorAbility::new);

    public static final RegistryObject<Ability> SCULKY_MECHA_MINES = ABILITIES.register("sculky_mecha_mines", Ability::new);
}
