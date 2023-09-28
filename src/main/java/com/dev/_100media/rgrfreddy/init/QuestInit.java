package com.dev._100media.rgrfreddy.init;

import com.dev._100media.rgrfreddy.RGRFreddy;
import com.dev._100media.rgrfreddy.quest.ExampleQuest;
import dev._100media.hundredmediaquests.init.HMQQuestInit;
import dev._100media.hundredmediaquests.quest.QuestType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class QuestInit {

    public static final DeferredRegister<QuestType<?>> QUESTS = DeferredRegister.create(HMQQuestInit.QUESTS.getRegistryKey(), RGRFreddy.MODID);

    public static final RegistryObject<QuestType<?>> MOUNTED_WRIST_ROCKETS = QUESTS.register("mounted_wrist_rockets", () -> QuestType.Builder.of(ExampleQuest::new).repeatable(false).instantTurnIn(false).build());
    public static final RegistryObject<QuestType<?>> MECHA_WARDEN_LASER = QUESTS.register("mecha_warden_laser", () -> QuestType.Builder.of(ExampleQuest::new).repeatable(false).instantTurnIn(false).build());
    public static final RegistryObject<QuestType<?>> SCULKY_MECHA_MINES = QUESTS.register("sculky_mecha_mines", () -> QuestType.Builder.of(ExampleQuest::new).repeatable(false).instantTurnIn(false).build());
    public static final RegistryObject<QuestType<?>> MECHA_MORPH = QUESTS.register("mecha_morph", () -> QuestType.Builder.of(ExampleQuest::new).repeatable(false).instantTurnIn(false).build());
    public static final RegistryObject<QuestType<?>> PORTABLE_TESLA_COIL = QUESTS.register("portable_tesla_coil", () -> QuestType.Builder.of(ExampleQuest::new).repeatable(false).instantTurnIn(false).build());
}
