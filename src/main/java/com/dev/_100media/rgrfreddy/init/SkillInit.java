package com.dev._100media.rgrfreddy.init;

import com.dev._100media.rgrfreddy.RGRFreddy;
import com.dev._100media.rgrfreddy.skill.MorphSkill;
import com.dev._100media.rgrfreddy.skill.tree.CombatTree;
import com.dev._100media.rgrfreddy.skill.tree.EvolutionTree;
import com.dev._100media.rgrfreddy.skill.tree.UtilityTree;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediaabilities.capability.AbilityHolderAttacher;
import dev._100media.hundredmediaabilities.capability.MarkerHolderAttacher;
import dev._100media.hundredmediaabilities.init.HMAAbilityInit;
import dev._100media.hundredmediaquests.init.HMQSkillsInit;
import dev._100media.hundredmediaquests.skill.Skill;
import dev._100media.hundredmediaquests.skill.SkillTree;
import dev._100media.hundredmediaquests.skill.defaults.MenuProvidingTree;
import dev._100media.hundredmediaquests.skill.defaults.QuestSkill;
import dev._100media.hundredmediaquests.skill.defaults.SimpleSkill;
import dev._100media.hundredmediaquests.skill.requirements.ItemSkillRequirement;
import net.minecraft.network.chat.Component;
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
            Component.literal("Mecha Skulk"),
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
    public static final RegistryObject<Skill> MOUNTED_WRIST_ROCKETS = SKILLS.register("mounted_wrist_rockets", () -> new QuestSkill(
            Component.literal("Mounted Wrist Rockets"),
            Component.literal("""
                    Point at any enemy within your vision while holding this item to begin locking onto them.
                    Once the lock on is finished, right click to fire 2 missiles out of your wrist that chase
                    the targeted enemy until detonation.
                    """),
            QuestInit.MOUNTED_WRIST_ROCKETS
    ));
    public static final RegistryObject<Skill> MECHA_WARDEN_LASER = SKILLS.register("mecha_warden_laser", () -> new QuestSkill(
            Component.literal("Mecha Warden Laser"),
            Component.literal("""
                    The Mecha Warden gets access to a powerful laser that can be fired from its mouth.
                    When you right-click with this ability, a wide beam of energy is fired forward similar
                    to the normal Warden Laser, but continuous. This beam damages any enemies in its path continuously.
                    SCULK: 12/s
                    COOLDOWN: 10s
                    """),
            QuestInit.MECHA_WARDEN_LASER
    ));
    public static final RegistryObject<Skill> SCULKY_MECHA_MINES = SKILLS.register("sculky_mecha_mines", () -> new QuestSkill(
            Component.literal("Sculky Mecha Mines"),
            Component.literal("""
                    Place down a mine below the Warden that blows up if an entity walks within 2 blocks of it.
                    The Mine does a massive explosion of 8 blocks with massive damage and transforms surface 
                    layer blocks into sculk blocks. Players standing on these sculk blocks will receive Slowness II 
                    and take automatic crits.
                    SCULK: 10
                    COOLDOWN: 3s
                    """),
            QuestInit.SCULKY_MECHA_MINES
    ));
    public static final RegistryObject<Skill> MECHA_MORPH = SKILLS.register("mecha_morph", () -> new QuestSkill(
            Component.literal("Mecha Morph"),
            Component.literal("""
                    The Mecha Warden unlocks the ability to transform into a stationary laser turret,
                    alongside the ability to mimic any block he has the ability to left click.
                    The turret's attack damage and attack speed depends on the users Evolution stage.
                    SCULK: 20
                    COOLDOWN: 5s
                    """),
            QuestInit.MECHA_MORPH
    ));
    public static final RegistryObject<Skill> PORTABLE_TESLA_COIL = SKILLS.register("portable_tesla_coil", () -> new QuestSkill(
            Component.literal("Portable Tesla Coil"),
            Component.literal("""
                    The Mecha Warden gains a Tesla Coil on their back that can be toggled
                    with a hotkey. The Warden also has a 40 block aura that targets the
                    closest enemy and constantly zaps them for 1 heart.
                    SCULK: 5/s
                    """),
            QuestInit.PORTABLE_TESLA_COIL
    ));

    public static final RegistryObject<MenuProvidingTree> COMBAT_TREE = SKILL_TREES.register("combat", () -> new CombatTree(
            Component.literal("Combat"),
            Arrays.asList(MOUNTED_WRIST_ROCKETS, MECHA_WARDEN_LASER, SCULKY_MECHA_MINES, MECHA_MORPH, PORTABLE_TESLA_COIL)
    ));
    // Utility
    public static final RegistryObject<Skill> MECHA_BOARD = SKILLS.register("mecha_board", () -> new SimpleSkill(
            Component.literal("Mecha Board"),
            Component.literal("""
                    The Mecha Warden transforms its robotic legs into a Hover Board that works similarly to
                    creative flight, but has a height limit and speed limit per Evolution.
                    """),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.SHIELD, 1),
                    new ItemSkillRequirement(() -> Items.REPEATER, 2),
                    new ItemSkillRequirement(() -> Items.POWERED_RAIL, 12)
            ),
            player -> {

            },
            player -> {

            }
    ));
    public static final RegistryObject<Skill> SCULKY_SNACK = SKILLS.register("sculky_snack", () -> new SimpleSkill(
            Component.literal("Sculky Snack"),
            Component.literal("""
                    A sculky snack that when eaten by the Mecha Warden gives him 5 hunger bars,
                    regeneration 2 for 5 seconds, and Resistance (level equivalent to Evolution tier) 
                    for 4 seconds.
                    COOLDOWN: 5s
                    """),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.GLOW_BERRIES, 10),
                    new ItemSkillRequirement(() -> Items.FERMENTED_SPIDER_EYE, 2),
                    new ItemSkillRequirement(() -> Items.SCULK_VEIN, 12)
            ),
            player -> {

            },
            player -> {
            }
    ));
    public static final RegistryObject<Skill> FUSION_REACTOR_CORE = SKILLS.register("fusion_reactor_core", () -> new SimpleSkill(
            Component.literal("Fusion Reactor Core Ejection"),
            Component.literal("""
                    Upon activation, this ability shoots a reactor core up into the air (like a mortar) that lands where
                    the Mecha Warden is looking. After 5 seconds on the ground, a mini-nuclear explosion destroys
                    everything within a 60 block circumference of the reactor core
                    SCULK: 80
                    COOLDOWN: 40s
                    """),
            Arrays.asList(
                    /*  new ItemTreasureMapSkillRequirement(1),*/
                    new ItemSkillRequirement(() -> Items.RECOVERY_COMPASS, 1),
                    new ItemSkillRequirement(() -> Items.RESPAWN_ANCHOR, 1),
                    new ItemSkillRequirement(() -> Items.TNT, 8)
            ),
            player -> {
                unlockAbility(player, AbilityInit.FUSION_CORE_REACTOR.get());
            },
            player -> {
                removeAbility(player, AbilityInit.FUSION_CORE_REACTOR.get());
            }
    ));

    public static final RegistryObject<Skill> MECHOLOCATION = SKILLS.register("mecholocation", () -> new SimpleSkill(
            Component.literal("Mecholocation"),
            Component.literal("""
                    Once unlocked, the Mecha Warden can use this item to “scan” the area in a 50 block radius.
                    If no players are in that 50 block radius, the Mecholocator will show a "title" of the
                    nearest hunter that is x amount of blocks away in y direction. If one or more players is found,
                    it will instead highlight those players in red for the Mecha Warden until their next death.
                    SCULK: 20
                    COOLDOWN: 10s
                    """),
            Arrays.asList(
                   /* new ItemTreasureMapSkillRequirement(1),*/
                    new ItemSkillRequirement(() -> Items.DAYLIGHT_DETECTOR, 10),
                    new ItemSkillRequirement(() -> Items.LODESTONE, 1)
            ),
            player -> {

            },
            player -> {
            }
    ));
    public static final RegistryObject<Skill> DEEP_DARK_DESTROYER = SKILLS.register("deep_dark_destroyer", () -> new SimpleSkill(
            Component.literal("Deep Dark Destroyer"),
            Component.literal("""
                    This shoulder mounted blaster shoots out a large blue orb of charged magnetic pulse.
                    Upon impact with an enemy or block, the orb should grow until it is 10 blocks wide,
                    sucking in enemies and soft blocks nearby, excluding the Mecha Warden. Enemies within
                    the grasp of the Deep Dark Destroyer orb are given blindness and take constant damage \
                    until death.
                    COOLDOWN: 15s
                    """),
            Arrays.asList(
                    new ItemSkillRequirement(() -> Items.HEART_OF_THE_SEA, 1),
                    new ItemSkillRequirement(() -> Items.NAUTILUS_SHELL, 1),
                    new ItemSkillRequirement(() -> Items.WITHER_SKELETON_SKULL, 1),
                    new ItemSkillRequirement(() -> Items.DRAGON_EGG, 1)
            ),
            player -> {

            },
            player -> {
            }
    ));

    public static final RegistryObject<MenuProvidingTree> UTILITY_TREE = SKILL_TREES.register("utility", () -> new UtilityTree(
            Component.literal("Utility"),
            Arrays.asList(MECHA_BOARD, SCULKY_SNACK, FUSION_REACTOR_CORE, MECHOLOCATION, DEEP_DARK_DESTROYER)
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
