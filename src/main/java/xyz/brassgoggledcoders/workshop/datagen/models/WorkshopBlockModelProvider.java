package xyz.brassgoggledcoders.workshop.datagen.models;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import java.util.function.Function;

public class WorkshopBlockModelProvider extends ModelProvider<BlockModelBuilder> {

    public WorkshopBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, "block", BlockModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //for(RegistryObject<Block> fluidBlock : WorkshopFluids.getAllFluids()) {
        //    this.singleTexture(fluidBlock.getId().getPath(), mcLoc(BLOCK_FOLDER), mcLoc("block/water_still"));
        //}
    }

    @Override
    public String getName() {
        return "Workshop Block Model Provider";
    }
}
