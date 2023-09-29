package dev._100media.rgrfreddy.init;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.block.HundredMediaBlock;
import dev._100media.rgrfreddy.block.MysticMusicBoxBlock;
import dev._100media.rgrfreddy.block.ToyBoxTrapBlock;
import dev._100media.rgrfreddy.block.entity.MysticMusicBoxBE;
import dev._100media.rgrfreddy.block.entity.ToyBoxTrapBE;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    public static final RegistryObject<Block> TOY_BOX_TRAP_BLOCK = registerBlock("toy_box_trap", () -> new ToyBoxTrapBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F).noOcclusion().noCollission().isValidSpawn((a, b, c, d) -> false)));

    public static final RegistryObject<Block> MYSTIC_MUSIC_BOX_BLOCK = registerBlock("mystic_music_box", () -> new MysticMusicBoxBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F).noOcclusion().noCollission().isValidSpawn((a, b, c, d) -> false)));

    public static final RegistryObject<BlockEntityType<ToyBoxTrapBE>> TOY_BOX_TRAP_BE = BLOCK_ENTITIES.register("toy_box_trap_be", () -> BlockEntityType.Builder.of(ToyBoxTrapBE::new, TOY_BOX_TRAP_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<MysticMusicBoxBE>> MYSTIC_MUSIC_BOX_BE = BLOCK_ENTITIES.register("mystic_music_box_be", () -> BlockEntityType.Builder.of(MysticMusicBoxBE::new, MYSTIC_MUSIC_BOX_BLOCK.get()).build(null));


    protected static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, b -> () -> new BlockItem(b.get(), ItemInit.getItemProperties()));
    }

    protected static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<T>, Supplier<? extends BlockItem>> item) {
        var reg = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> item.apply(reg).get());
        return reg;
    }
}
