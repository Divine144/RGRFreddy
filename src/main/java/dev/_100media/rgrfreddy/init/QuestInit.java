package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.quest.*;
import dev._100media.hundredmediaquests.init.HMQQuestInit;
import dev._100media.hundredmediaquests.quest.QuestType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class QuestInit {

    public static final DeferredRegister<QuestType<?>> QUESTS = DeferredRegister.create(HMQQuestInit.QUESTS.getRegistryKey(), RGRFreddy.MODID);

    public static final RegistryObject<QuestType<?>> FREDDY_MICROPHONE = QUESTS.register("freddy_microphone", () -> QuestType.Builder.of(FreddyMicrophoneQuest::new).repeatable(false).instantTurnIn(false).build());
    public static final RegistryObject<QuestType<?>> FREDDY_HAT = QUESTS.register("freddy_hat", () -> QuestType.Builder.of(FreddyHatQuest::new).repeatable(false).instantTurnIn(false).build());
    public static final RegistryObject<QuestType<?>> TOY_BOX_TRAP = QUESTS.register("toy_box_trap", () -> QuestType.Builder.of(ToyBoxTrapQuest::new).repeatable(false).instantTurnIn(false).build());
    public static final RegistryObject<QuestType<?>> TOY_ARMY = QUESTS.register("toy_army", () -> QuestType.Builder.of(ToyArmyQuest::new).repeatable(false).instantTurnIn(false).build());
    public static final RegistryObject<QuestType<?>> FREDDY_PIZZA = QUESTS.register("freddy_pizza", () -> QuestType.Builder.of(FreddyPizzaQuest::new).repeatable(false).instantTurnIn(false).build());
}
