package dev._100media.rgrfreddy.init;

import dev._100media.hundredmediaquests.skill.requirements.ItemTagSkillRequirement;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.requirement.EnchantedItemRequirement;
import dev._100media.rgrfreddy.skill.MorphSkill;
import dev._100media.rgrfreddy.skill.tree.CombatTree;
import dev._100media.rgrfreddy.skill.tree.EvolutionTree;
import dev._100media.rgrfreddy.skill.tree.UtilityTree;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediaabilities.capability.AbilityHolderAttacher;
import dev._100media.hundredmediaabilities.init.HMAAbilityInit;
import dev._100media.hundredmediaquests.init.HMQSkillsInit;
import dev._100media.hundredmediaquests.skill.Skill;
import dev._100media.hundredmediaquests.skill.SkillTree;
import dev._100media.hundredmediaquests.skill.defaults.MenuProvidingTree;
import dev._100media.hundredmediaquests.skill.defaults.QuestSkill;
import dev._100media.hundredmediaquests.skill.defaults.SimpleSkill;
import dev._100media.hundredmediaquests.skill.requirements.ItemSkillRequirement;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

public class SkillInit {
    public static final DeferredRegister<SkillTree> SKILL_TREES = DeferredRegister.create(HMQSkillsInit.SKILL_TREES.getRegistryName(), RGRFreddy.MODID);
    public static final DeferredRegister<Skill> SKILLS = DeferredRegister.create(HMQSkillsInit.SKILLS.getRegistryName(), RGRFreddy.MODID);

    // Evolution
    public static final RegistryObject<Skill> KID_FREDDY = SKILLS.register("kid_freddy", () -> new MorphSkill(
            Component.literal("Kid Freddy"),
            Component.literal("%s Hearts".formatted(5)),
            Arrays.asList(),
            MorphInit.KID_FREDDY
    ));
    public static final RegistryObject<Skill> TOY_FREDDY = SKILLS.register("toy_freddy", () -> new MorphSkill(
            Component.literal("Toy Freddy"),
            Component.literal("%s Hearts, Strength %s, Speed %s, Jump Boost %s, %s".formatted(15, "I", "I", "I", "Blindness Immunity")),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.PORKCHOP, 1),
                    new ItemSkillRequirement(() -> Items.BEEF, 1),
                    new ItemSkillRequirement(() -> Items.MUTTON, 1),
                    new ItemSkillRequirement(() -> Items.RABBIT, 1),
                    new ItemSkillRequirement(() -> Items.CHICKEN, 1),
                    new ItemSkillRequirement(() -> Items.ROTTEN_FLESH, 1),
                    new ItemSkillRequirement(() -> Items.DIAMOND, 5),
                    new ItemSkillRequirement(() -> Items.NAME_TAG, 1)
            ),
            MorphInit.TOY_FREDDY
    ));
    public static final RegistryObject<Skill> FREDDY_FAZBEAR = SKILLS.register("freddy_fazbear", () -> new MorphSkill(
            Component.literal("Freddy Fazbear"),
            Component.literal("%s Hearts, Strength %s, Speed %s, Jump Boost %s, %s, +%s Blocks of Reach".formatted(25, "II", "II", "II", "Blindness Immunity", 2)),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.REDSTONE, 64),
                    new ItemSkillRequirement(() -> Items.OBSIDIAN, 12),
                    new ItemSkillRequirement(() -> Items.WITHER_SKELETON_SKULL, 3),
                    new ItemSkillRequirement(() -> Items.RECOVERY_COMPASS, 1)
            ),
            MorphInit.FREDDY_FAZBEAR
    ));
    public static final RegistryObject<Skill> GOLDEN_FREDDY_FAZBEAR = SKILLS.register("golden_freddy_fazbear", () -> new MorphSkill(
            Component.literal("Golden Freddy Fazbear"),
            Component.literal("%s Hearts, Strength %s, Speed %s, Jump Boost %s, %s, +%s Blocks of Reach".formatted(40, "III", "III", "III", "Blindness Immunity", 4)),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.DIAMOND, 10),
                    new ItemSkillRequirement(() -> Items.GOLD_INGOT, 10),
                    new ItemSkillRequirement(() -> Items.GOLDEN_APPLE, 10),
                    new ItemSkillRequirement(() -> Items.ENCHANTED_GOLDEN_APPLE, 2)
            ),
            MorphInit.GOLDEN_FREDDY_FAZBEAR
    ));
    public static final RegistryObject<Skill> NIGHTMARE_FREDDY_FAZBEAR = SKILLS.register("nightmare_freddy_fazbear", () -> new MorphSkill(
            Component.literal("Nightmare Freddy"),
            Component.literal("%s Hearts, Strength %s, Speed %s, Jump Boost %s, %s, +%s Blocks of Reach, Flight".formatted(60, "V", "V", "IV", "Blindness Immunity", 8)),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.ELYTRA, 1),
                    new ItemSkillRequirement(() -> Items.HEART_OF_THE_SEA, 2),
                    new ItemSkillRequirement(() -> Items.TOTEM_OF_UNDYING, 3)
            ),
            MorphInit.NIGHTMARE_FREDDY_FAZBEAR
    ));

    public static final RegistryObject<MenuProvidingTree> EVOLUTION_TREE = SKILL_TREES.register("evolution", () -> new EvolutionTree(
            Component.literal("Evolution"),
            Arrays.asList(KID_FREDDY, TOY_FREDDY, FREDDY_FAZBEAR, GOLDEN_FREDDY_FAZBEAR, NIGHTMARE_FREDDY_FAZBEAR)
    ));
    // Combat
    public static final RegistryObject<Skill> FREDDY_MICROPHONE = SKILLS.register("freddy_microphone", () -> new QuestSkill(
            Component.literal("Freddy's Microphone"),
            Component.literal("""
                    A weapon that acts like a diamond sword. When right clicked, all nearby hunters nausea for 10 seconds.
                    COOLDOWN: 30s
                    """),
            QuestInit.FREDDY_MICROPHONE
    ));
    public static final RegistryObject<Skill> FREDDY_HAT = SKILLS.register("freddy_hat", () -> new QuestSkill(
            Component.literal("Freddy's Hat"),
            Component.literal("""
                    A weapon that can be thrown at the hunters. When thrown, the camera will follow the hat.
                    The hat will be controllable by Freddy, guided like a cruise missile.
                    If the hat lands within 3 blocks of the hunters, it will attach to one. Then, Freddy will be able to
                    control this hunter. Control lasts for 1 minute.
                    COOLDOWN: 120s
                    """),
            QuestInit.FREDDY_HAT
    ));
    public static final RegistryObject<Skill> TOY_ARMY = SKILLS.register("toy_army", () -> new QuestSkill(
            Component.literal("Toy Army"),
            Component.literal("""
                    Summon a group of 10 miniature Toy Freddy's that chase / attack the hunters.
                    COOLDOWN: 60s
                    """),
            QuestInit.TOY_ARMY
    ));
    public static final RegistryObject<Skill> TOY_BOX_TRAP = SKILLS.register("toy_box_trap", () -> new QuestSkill(
            Component.literal("Toy Box Trap"),
            Component.literal("""
                    Place a toy box as a trap on the ground where Freddy is looking. If a hunter gets anywhere within 10
                    blocks of the toy box, they start getting sucked into the toy box.  When a player is sucked fully into the Toy Box,
                    they cannot move and a timer begins playing on their screen counting down from 20. Once it reaches 0,
                    they will be teleported to the pizzeria.
                    """),
            QuestInit.TOY_BOX_TRAP
    ));
    public static final RegistryObject<Skill> FREDDY_PIZZA = SKILLS.register("freddy_pizza", () -> new QuestSkill(
            Component.literal("Freddy's Pizza"),
            Component.literal("""
                    Freddy gets a box of pizza, that when right click is hit or held down, rapidly shoots out pizza
                    that hones in on the nearest player or entity. When the pizza hits a player it explodes doing
                    massive damage to players and blocks.
                    """),
            QuestInit.FREDDY_PIZZA
    ));

    public static final RegistryObject<MenuProvidingTree> COMBAT_TREE = SKILL_TREES.register("combat", () -> new CombatTree(
            Component.literal("Combat"),
            Arrays.asList(FREDDY_MICROPHONE, FREDDY_HAT, TOY_BOX_TRAP, TOY_ARMY, FREDDY_PIZZA)
    ));
    // Utility
    public static final RegistryObject<Skill> EERIE_LULLABY = SKILLS.register("eerie_lullaby", () -> new SimpleSkill(
            Component.literal("Eerie Lullaby"),
            Component.literal("""
                    When this ability is used, all enemies within a 20 block diameter are frozen with fear. And their
                    screens should begin to shake. The amount of time they are feared depends on evolution level.
                    """),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.SNIFFER_EGG, 1),
                    new ItemSkillRequirement(() -> Items.IRON_INGOT, 64),
                    new ItemSkillRequirement(() -> Items.SALMON, 32)
            ),
            player -> {
                unlockAbility(player, AbilityInit.EERIE_LULLABY.get());
            },
            player -> {
                removeAbility(player, AbilityInit.EERIE_LULLABY.get());
            }
    ));
    public static final RegistryObject<Skill> MYSTIC_MUSIC_BOX = SKILLS.register("mystic_music_box", () -> new SimpleSkill(
            Component.literal("Mystic Music Box"),
            Component.literal("""
                    Activate a mystical music box that appears at Freddy’s feet when activated. The music box lulls enemy hunters into a trance,
                    causing them to get slowness 2, and scramble their controls when within 20 blocks.
                    Also, Freddy obtains Speed 10 and Strength 2 levels higher than what it is based from evolution.
                    """),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.JUKEBOX, 1),
                    new ItemSkillRequirement(() -> Items.EMERALD, 16),
                    new ItemSkillRequirement(() -> Items.NAUTILUS_SHELL, 1)
            ),
            player -> {
                player.getInventory().add(new ItemStack(BlockInit.MYSTIC_MUSIC_BOX_BLOCK.get()));
            },
            player -> {
            }
    ));
    public static final RegistryObject<Skill> SMOKE_BOMBS = SKILLS.register("smoke_bombs", () -> new SimpleSkill(
            Component.literal("Smoke Bombs"),
            Component.literal("""
                    Deploy a smoke bomb that create a thick fog, obscuring vision and disorienting enemy hunters, allowing Freddy to close the gap unnoticed.
                    When in the smoke, hunters are given blindness for 20 seconds. Additionally, Freddy turns invisible for 30 seconds.
                    """),
            Arrays.asList(
                    /*  new ItemTreasureMapSkillRequirement(1),*/
                    new ItemSkillRequirement(() -> Items.GRAY_DYE, 10),
                    new ItemSkillRequirement(() -> Items.LAPIS_LAZULI, 10),
                    new ItemSkillRequirement(() -> Items.REDSTONE, 5)
            ),
            player -> {
                player.getInventory().add(new ItemStack(ItemInit.SMOKE_BOMB.get()));
            },
            player -> {

            }
    ));

    public static final RegistryObject<Skill> FREDDY_SNARES = SKILLS.register("freddy_snares", () -> new SimpleSkill(
            Component.literal("Freddy's Snares"),
            Component.literal("""
                    Place down a snare where Freddy is looking. This snare will activate whenever a player (Except Freddy) steps on it;
                    they instantly go blind with darkness, cant move with slowness, and their coordinates are broadcast to the entire server.
                    Heartbeats will start playing for anyone within 20 blocks of the snare, once it's been activated.
                    """),
            Arrays.asList(
                    new EnchantedItemRequirement(p -> p.isEnchanted() && p.is(Items.TRIDENT), "Enchanted Trident"),
                    new ItemSkillRequirement(() -> Items.TRIPWIRE_HOOK, 32),
                    new ItemSkillRequirement(() -> Items.RABBIT_FOOT, 2)
            ),
            player -> {
                unlockAbility(player, AbilityInit.FREDDY_SNARES.get());
            },
            player -> {
                removeAbility(player, AbilityInit.FREDDY_SNARES.get());
            }
    ));
    public static final RegistryObject<Skill> DIMENSIONAL_TRAPDOOR = SKILLS.register("dimensional_trapdoor", () -> new SimpleSkill(
            Component.literal("Dimensional Trapdoor"),
            Component.literal("""
                    Freddy can set 2 teleporters down; that teleport freddy from Portal A to Portal B. These portals should look like a trap door
                    When Freddy jumps in, Freddy should pop up next to the other portal, allowing Freddy to teleport around. If the hunters
                    try to enter these doors, they will instantly die.
                    """),
            Arrays.asList(
                    new ItemTagSkillRequirement(() -> ItemTags.TRAPDOORS, 32, Component.literal("[Trapdoor]")),
                    new ItemSkillRequirement(() -> Items.DRAGON_EGG, 1),
                    new ItemSkillRequirement(() -> Items.DRAGON_HEAD, 1),
                    new ItemSkillRequirement(() -> Items.END_CRYSTAL, 4)
            ),
            player -> {
                unlockAbility(player, AbilityInit.DIMENSIONAL_TRAPDOOR.get());
            },
            player -> {
                removeAbility(player, AbilityInit.DIMENSIONAL_TRAPDOOR.get());
            }
    ));

    public static final RegistryObject<MenuProvidingTree> UTILITY_TREE = SKILL_TREES.register("utility", () -> new UtilityTree(
            Component.literal("Utility"),
            Arrays.asList(EERIE_LULLABY, MYSTIC_MUSIC_BOX, SMOKE_BOMBS, FREDDY_SNARES, DIMENSIONAL_TRAPDOOR)
    ));


    public static void unlockAbility(Player player, Ability abilityToUnlock) {
        AbilityHolderAttacher.getAbilityHolder(player).ifPresent(holder -> {
            int index = -1;
            boolean hasAbility = false;
            for (int i = 0; i < holder.getAbilitiesSize(); ++i) {
                Ability ability = holder.getAbility(i);
                if (index == -1 && ability == HMAAbilityInit.NONE.get()) {
                    index = i;
                }
                if (ability == abilityToUnlock) {
                    hasAbility = true;
                    break;
                }
            }
            if (index != -1 && !hasAbility) {
                holder.setAbility(index, abilityToUnlock);
            }
        });
    }

    public static void removeAbility(Player player, Ability ability) {
        AbilityHolderAttacher.getAbilityHolder(player).ifPresent(holder -> {
            int i = holder.getAbilities().indexOf(ability);
            if (i == -1) {
                return;
            }
            holder.setAbility(i, HMAAbilityInit.NONE.get());
        });
    }
}
