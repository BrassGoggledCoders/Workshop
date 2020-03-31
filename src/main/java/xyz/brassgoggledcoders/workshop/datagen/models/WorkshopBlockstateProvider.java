package xyz.brassgoggledcoders.workshop.datagen.models;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

public class WorkshopBlockstateProvider extends BlockStateProvider {

    private final ExistingFileHelper helper;

    public WorkshopBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Workshop.MOD_ID, exFileHelper);
        this.helper = exFileHelper;
    }

    public static ResourceLocation getModel(Block block) {
        return new ResourceLocation(block.getRegistryName().getNamespace(), "block/" + block.getRegistryName().getPath());
    }

    @Override
    protected void registerStatesAndModels() {
        for(RegistryObject<Fluid> fluid : WorkshopFluids.getAllFluids()) {
            /*getVariantBuilder(fluidBlock.get())
                    .partialState()
                    .setModels(ConfiguredModel
                            .builder()
                            .modelFile(new ModelFile.ExistingModelFile(getModel(fluidBlock.get()), helper)).buildLast());*/
        }
    }
}
