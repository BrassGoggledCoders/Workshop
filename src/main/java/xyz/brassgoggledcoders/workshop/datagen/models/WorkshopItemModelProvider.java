package xyz.brassgoggledcoders.workshop.datagen.models;

import com.hrznstudio.titanium.Titanium;
import net.minecraft.block.Block;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.BlockRegistryObjectGroup;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
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
        for(BlockRegistryObjectGroup concrete : WorkshopBlocks.CONCRETES) {
            this.withExistingParent(concrete.getItem().getRegistryName().getPath(), modLoc(BLOCK_FOLDER + "/" + concrete.getName()));
        }
        //Manually for now
        //TODO these two need to be in titaniun folder
        this.singleTexture("iron_dust", mcLoc("item/generated"), new ResourceLocation(Titanium.MODID, "items/resource/dust"));
        this.singleTexture("gold_dust", mcLoc("item/generated"), new ResourceLocation(Titanium.MODID, "items/resource/dust"));
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
