package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.block.*;
import dev._100media.rgrfreddy.block.entity.*;
import dev._100media.rgrfreddy.item.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RGRFreddy.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RGRFreddy.MODID);

    public static final RegistryObject<HundredMediaBlock> HUNDRED_MEDIA = registerBlock("hundred_media", () -> new HundredMediaBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.RED).noOcclusion().strength(1)));

    public static final RegistryObject<Block> TOY_BOX_TRAP_BLOCK = registerBlock("toy_box_trap", () -> new ToyBoxTrapBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F).noOcclusion().noCollission().isValidSpawn((a, b, c, d) -> false)),
            b -> () -> new BoxTrapBlockItem(b.get(), ItemInit.getItemProperties()));

    public static final RegistryObject<Block> MYSTIC_MUSIC_BOX_BLOCK = registerBlock("mystic_music_box", () -> new MysticMusicBoxBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F).noOcclusion().noCollission().isValidSpawn((a, b, c, d) -> false)),
            b -> () -> new MusicBoxBlockItem(b.get(), ItemInit.getItemProperties()));

    public static final RegistryObject<Block> SNARE_BLOCK = registerBlock("snare", () -> new SnareBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(50.0F, 1200.0F)
            .requiresCorrectToolForDrops().noOcclusion().noCollission().isValidSpawn((a, b, c, d) -> false)),
            b -> () -> new SnareBlockItem(b.get(), ItemInit.getItemProperties()));

    public static final RegistryObject<Block> DIMENSIONAL_TRAPDOOR_BLOCK = registerBlock("dimensional_door", () -> new DimensionalTrapDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().isValidSpawn((a, b, c, d) -> false).ignitedByLava()));

    public static final RegistryObject<Block> JAIL_DOOR_BLOCK = registerBlock("jail_door", () -> new JailDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().isValidSpawn((a, b, c, d) -> false)),
            b -> () -> new JailDoorBlockItem(b.get(), ItemInit.getItemProperties()));

    public static final RegistryObject<Block> EMP_GENERATOR_BLOCK = registerBlock("emp_generator", () -> new EMPGeneratorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().isValidSpawn((a, b, c, d) -> false)),
            b -> () -> new GeneratorBlockItem(b.get(), ItemInit.getItemProperties()));

    public static final RegistryObject<Block> SMOKE_BOMB_BLOCK = registerBlock("smoke_bomb", () -> new SmokeBombBlock(BlockBehaviour.Properties.of().noCollission().noLootTable().air()));

    public static final RegistryObject<BlockEntityType<ToyBoxTrapBE>> TOY_BOX_TRAP_BE = BLOCK_ENTITIES.register("toy_box_trap_be", () -> BlockEntityType.Builder.of(ToyBoxTrapBE::new, TOY_BOX_TRAP_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<MysticMusicBoxBE>> MYSTIC_MUSIC_BOX_BE = BLOCK_ENTITIES.register("mystic_music_box_be", () -> BlockEntityType.Builder.of(MysticMusicBoxBE::new, MYSTIC_MUSIC_BOX_BLOCK.get(), SMOKE_BOMB_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<SnareBE>> SNARE_BE = BLOCK_ENTITIES.register("snare_be", () -> BlockEntityType.Builder.of(SnareBE::new, SNARE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<DimensionalTrapDoorBE>> DIMENSIONAL_TRAPDOOR_BE = BLOCK_ENTITIES.register("dimensional_door_be", () -> BlockEntityType.Builder.of(DimensionalTrapDoorBE::new, DIMENSIONAL_TRAPDOOR_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<JailDoorBE>> JAIL_DOOR_BE = BLOCK_ENTITIES.register("jail_door_be", () -> BlockEntityType.Builder.of(JailDoorBE::new, JAIL_DOOR_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<EMPGeneratorBE>> EMP_GENERATOR_BE = BLOCK_ENTITIES.register("emp_generator_be", () -> BlockEntityType.Builder.of(EMPGeneratorBE::new, EMP_GENERATOR_BLOCK.get()).build(null));


    protected static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, b -> () -> new BlockItem(b.get(), ItemInit.getItemProperties()));
    }

    protected static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<T>, Supplier<? extends BlockItem>> item) {
        var reg = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> item.apply(reg).get());
        return reg;
    }
}
