package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectInit {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, RGRFreddy.MODID);

/*    public static final RegistryObject<MobEffect> LOCK_ON = EFFECTS.register("lock_on", SimpleEffect::new);

    public static final RegistryObject<MobEffect> NETTED = EFFECTS.register("netted", () -> new NettedEffect(MobEffectCategory.HARMFUL, 0xFF964B00));*/
}
