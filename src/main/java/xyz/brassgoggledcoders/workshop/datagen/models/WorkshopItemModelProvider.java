package xyz.brassgoggledcoders.workshop.datagen.models;

import net.minecraft.block.Block;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import javax.annotation.Resource;
import java.util.function.BiFunction;
import java.util.function.Function;

public class WorkshopItemModelProvider extends ModelProvider<PropertiedItemModelBuilder> {

    public WorkshopItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, ITEM_FOLDER, PropertiedItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(RegistryObject<Fluid> fluid : WorkshopFluids.getAllFluids()) {
            if(fluid.get().isSource(fluid.get().getDefaultState())) {
                bucket(fluid.getId());
            }
        }
    }

    private void bucket(ResourceLocation fluidName) {
        getBuilder(fluidName.toString() + "_bucket")
                .parent(new ModelFile.UncheckedModelFile("forge:" + ITEM_FOLDER + "/bucket_drip"))
                .property("loader", "forge:bucket")
                .property("fluid", fluidName.toString());
    }

    @Override
    public String getName() {
        return "Workshop Item Model Provider";
    }
}
