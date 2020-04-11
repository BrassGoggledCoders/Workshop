package xyz.brassgoggledcoders.workshop.datagen.models;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

public class WorkshopBlockModelProvider extends ModelProvider<BlockModelBuilder> {

    public WorkshopBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, "block", BlockModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(RegistryObject<Fluid> fluid : WorkshopFluids.getAllFluids()) {
            if(fluid.get().isSource(fluid.get().getDefaultState())) {
                this.singleTexture(fluid.getId().getPath(), mcLoc("water"), "particle", mcLoc("block/water_still"));
            }
        }
        for(BlockRegistryObjectGroup concrete : WorkshopBlocks.CONCRETES) {
            this.cubeAll(concrete.getName(), new ResourceLocation("minecraft", BLOCK_FOLDER + "/" + concrete.getName().replace("_rebarred_", "_")));
        }
    }

    @Override
    public String getName() {
        return "Workshop Block Model Provider";
    }
}
