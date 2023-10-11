package dev._100media.rgrfreddy.datagen;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.init.*;
import com.google.common.collect.ImmutableMap;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediaabilities.marker.Marker;
import dev._100media.hundredmediamorphs.morph.Morph;
import dev._100media.rgrfreddy.init.*;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ModLangProvider extends LanguageProvider {
    protected static final Map<String, String> REPLACE_LIST = ImmutableMap.of(
            "tnt", "TNT",
            "sus", ""
    );

    public ModLangProvider(PackOutput gen) {
        super(gen, RGRFreddy.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ItemInit.ITEMS.getEntries().forEach(this::itemLang);
        EntityInit.ENTITIES.getEntries().forEach(this::entityLang);
        BlockInit.BLOCKS.getEntries().forEach(this::blockLang);
        AbilityInit.ABILITIES.getEntries().forEach(this::abilityLang);
        MarkerInit.MARKERS.getEntries().forEach(this::markerLang);
        MorphInit.MORPHS.getEntries().forEach(this::morphLang);
        add("itemGroup.hundredMediaTab", "100 Media");
        add("key.rgrfreddy.skill_tree", "Open Skill Tree");
        add("key.category.rgrfreddy", "RGRFreddy");
        add("effect.rgrfreddy.netted", "Targeted");

        // Quest Descriptions
        add("quest.goal.rgrfreddy.get_enchanted_golden_item_goal.description", "Get an Enchanted Golden Item");
        add("quest.goal.rgrfreddy.get_music_disc_goal.description", "Get a music disc");
        add("quest.goal.rgrfreddy.control_player_to_lava_goal.description", "Control a Player into Lava");
        add("quest.goal.rgrfreddy.craft_disc_goal.description", "Craft Music Disc 5");
        add("quest.goal.rgrfreddy.hit_players_microphone_goal.description", "Deal 100 Total Damage to players with your Microphone");
        add("quest.goal.rgrfreddy.give_parrot_cookie_goal.description", "Give a Parrot a Cookie");
        add("quest.goal.rgrfreddy.hit_players_goal.description", "Hit Players 30 Times");
        add("quest.goal.rgrfreddy.punch_warden_goal.description", "Punch a Warden 10 Times");
        add("quest.goal.rgrfreddy.jumpscare_players_goal.description", "Jumpscare 3 Players");
        add("quest.goal.rgrfreddy.stay_near_hunter_goal.description", "Stay Near Hunters for 120 Seconds");
        add("quest.goal.rgrfreddy.tame_wolf_goal.description", "Tame 3 Wolves");
        add("quest.goal.rgrfreddy.trade_emeralds_goal.description", "Trade 50 Emeralds to Villagers");


        // Quest Display Descriptions
        add("quest.goal.rgrfreddy.get_enchanted_golden_item_goal", "Get an Enchanted Golden Item");
        add("quest.goal.rgrfreddy.get_music_disc_goal", "Get a music disc");
        add("quest.goal.rgrfreddy.control_player_to_lava_goal", "Control a Player into Lava");
        add("quest.goal.rgrfreddy.craft_disc_goal", "Craft Music Disc 5");
        add("quest.goal.rgrfreddy.hit_players_microphone_goal", "Deal Damage to Players With Your Microphone");
        add("quest.goal.rgrfreddy.give_parrot_cookie_goal", "Give a Parrot a Cookie");
        add("quest.goal.rgrfreddy.hit_players_goal", "Hit Players");
        add("quest.goal.rgrfreddy.punch_warden_goal", "Punch a Warden 10 Times");
        add("quest.goal.rgrfreddy.jumpscare_players_goal", "Jumpscare Players");
        add("quest.goal.rgrfreddy.stay_near_hunter_goal", "Stay Near Hunters for 120 Seconds");
        add("quest.goal.rgrfreddy.tame_wolf_goal", "Tame Wolves");
        add("quest.goal.rgrfreddy.trade_emeralds_goal", "Trade Emeralds to Villagers");
    }

    protected void itemLang(RegistryObject<Item> entry) {
        if (!(entry.get() instanceof BlockItem) || entry.get() instanceof ItemNameBlockItem) {
            addItem(entry, checkReplace(entry));
        }
    }

    protected void blockLang(RegistryObject<Block> entry) {
        addBlock(entry, checkReplace(entry));
    }

    protected void entityLang(RegistryObject<EntityType<?>> entry) {
        addEntityType(entry, checkReplace(entry));
    }

    protected void abilityLang(RegistryObject<Ability> entry) {
        add(entry.get().getDescriptionId(), checkReplace(entry));
    }

    protected void markerLang(RegistryObject<Marker> entry) {
        add(entry.get().getDescriptionId(), checkReplace(entry));
    }

    protected void morphLang(RegistryObject<Morph> entry) {
        add(entry.get().getDescriptionId(), checkReplace(entry));
    }


    protected String checkReplace(RegistryObject<?> registryObject) {
        return Arrays.stream(registryObject.getId().getPath().split("_"))
                .map(this::checkReplace)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "))
                .trim();
    }

    protected String checkReplace(String string) {
        return REPLACE_LIST.containsKey(string) ? REPLACE_LIST.get(string) : StringUtils.capitalize(string);
    }
}
