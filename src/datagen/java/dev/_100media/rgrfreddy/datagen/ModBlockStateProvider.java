package dev._100media.rgrfreddy.datagen;

import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, RGRFreddy.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // Stream.of(
        //
        //         )
        //         .map(Supplier::get)
        //         .forEach(this::simpleCubeBottomTopBlockState);
        //
        Stream.of(BlockInit.SNARE_BLOCK, BlockInit.JAIL_DOOR_BLOCK, BlockInit.TOY_BOX_TRAP_BLOCK, BlockInit.MYSTIC_MUSIC_BOX_BLOCK, BlockInit.EMP_GENERATOR_BLOCK)
                .map(Supplier::get)
                .forEach(this::simpleBlock);

    }

    protected void simpleCubeBottomTopBlockState(Block block) {
        simpleBlock(block, blockCubeTopModel(block));
    }

    protected BlockModelBuilder blockCubeTopModel(Block block) {
        String name = getName(block);
        return models().cubeBottomTop(name, modLoc("block/" + name + "_side"), modLoc("block/" + name + "_bottom"), modLoc("block/" + name + "_top"));
    }

    protected String getName(Block item) {
        return ForgeRegistries.BLOCKS.getKey(item).getPath();
    }
}
