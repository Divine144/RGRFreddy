package com.dev._100media.rgrfreddy;

import com.dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import com.dev._100media.rgrfreddy.config.ExampleClientConfig;
import com.dev._100media.rgrfreddy.config.ExampleConfig;
import com.dev._100media.rgrfreddy.init.*;
import com.dev._100media.rgrfreddy.network.NetworkHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(RGRFreddy.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RGRFreddy {
    public static final String MODID = "rgrfreddy";
    public static final Logger LOGGER = LogManager.getLogger();

    public RGRFreddy() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ExampleConfig.CONFIG_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ExampleClientConfig.CLIENT_SPEC);

        ItemInit.ITEMS.register(modBus);
        EntityInit.ENTITIES.register(modBus);
        EffectInit.EFFECTS.register(modBus);
        BlockInit.BLOCKS.register(modBus);
        SoundInit.SOUNDS.register(modBus);
        BlockInit.BLOCK_ENTITIES.register(modBus);
        MenuInit.MENUS.register(modBus);
        CreativeModeTabInit.CREATIVE_MODE_TABS.register(modBus);
        MorphInit.MORPHS.register(modBus);
        AbilityInit.ABILITIES.register(modBus);
        MarkerInit.MARKERS.register(modBus);
        QuestInit.QUESTS.register(modBus);
        SkillInit.SKILLS.register(modBus);
        SkillInit.SKILL_TREES.register(modBus);
        FreddyHolderAttacher.register();
        GeckoLib.initialize();
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }
}
