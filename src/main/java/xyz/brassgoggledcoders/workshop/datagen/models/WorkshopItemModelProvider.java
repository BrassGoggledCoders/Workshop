package xyz.brassgoggledcoders.workshop.datagen.models;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import javax.annotation.Nonnull;

public class WorkshopItemModelProvider extends ModelProvider<PropertiedItemModelBuilder> {

    public WorkshopItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, ITEM_FOLDER, PropertiedItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(RegistryObject<Fluid> fluid : WorkshopFluids.getAllFluids()) {
            if(fluid.get().isSource(fluid.get().getDefaultState())) {
                bucket(fluid.getId());
                bottle(fluid.getId());
            }
        }
        for(BlockRegistryObjectGroup<Block, BlockItem, ?> concrete : WorkshopBlocks.CONCRETES) {
            this.withExistingParent(concrete.getItem().getRegistryName().getPath(), modLoc(BLOCK_FOLDER + "/" + concrete.getName()));
        }
        //Manually for now TODO these need to be in titanium folder. Texture name needs to change from 'texture' to 'layer0'
        this.singleTexture("iron_dust", mcLoc("item/generated"), new ResourceLocation(Titanium.MODID, "items/resource/dust"));
        this.singleTexture("gold_dust", mcLoc("item/generated"), new ResourceLocation(Titanium.MODID, "items/resource/dust"));
        this.singleTexture("iron_pipe", mcLoc("item/generated"), modLoc("items/resource/pipe"));
        this.singleTexture("gold_pipe", mcLoc("item/generated"), modLoc("items/resource/pipe"));
        this.singleTexture("iron_film", mcLoc("item/generated"), modLoc("items/resource/film"));
        this.singleTexture("gold_film", mcLoc("item/generated"), modLoc("items/resource/film"));
    }

    private void bucket(ResourceLocation fluidName) {
        getBuilder(fluidName.toString() + "_bucket")
                .parent(new ModelFile.UncheckedModelFile("forge:" + ITEM_FOLDER + "/bucket_drip"))
                .property("loader", "forge:bucket")
                .property("fluid", fluidName.toString());
    }

    private void bottle(ResourceLocation fluidName) {
        getBuilder(fluidName.toString() + "_bottle")
                .parent(new ModelFile.UncheckedModelFile(modLoc(ITEM_FOLDER + "/bottle")))
                .property("loader", "forge:bucket")
                .property("fluid", fluidName.toString());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Item Model Provider";
    }
}
