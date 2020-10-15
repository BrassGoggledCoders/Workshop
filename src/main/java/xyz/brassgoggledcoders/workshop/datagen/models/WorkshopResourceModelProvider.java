package xyz.brassgoggledcoders.workshop.datagen.models;

import com.hrznstudio.titanium.Titanium;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelProvider;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopResourcePlugin;

public class WorkshopResourceModelProvider extends ModelProvider<PropertiedItemModelBuilder> {
    public WorkshopResourceModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Titanium.MODID, ITEM_FOLDER, PropertiedItemModelBuilder::new, existingFileHelper);
    }
    @Override
    protected void registerModels() {
        this.singleTexture("iron_dust", mcLoc("item/generated"), "layer0", new ResourceLocation(Titanium.MODID, "items/resource/dust"));
        this.singleTexture("gold_dust", mcLoc("item/generated"), "layer0", new ResourceLocation(Titanium.MODID, "items/resource/dust"));
        this.singleTexture("iron_pipe", mcLoc("item/generated"), "layer0", new ResourceLocation(Workshop.MOD_ID, "items/resource/pipe"));
        this.singleTexture("gold_pipe", mcLoc("item/generated"), "layer0", new ResourceLocation(Workshop.MOD_ID,"items/resource/pipe"));
        this.singleTexture("iron_film", mcLoc("item/generated"), "layer0", new ResourceLocation(Workshop.MOD_ID,"items/resource/film"));
        this.singleTexture("gold_film", mcLoc("item/generated"), "layer0", new ResourceLocation(Workshop.MOD_ID,"items/resource/film"));
        this.singleTexture("copper_nugget", mcLoc("item/generated"), "layer0", new ResourceLocation(Titanium.MODID, "items/resource/nugget"));
        this.singleTexture("silver_nugget", mcLoc("item/generated"), "layer0", new ResourceLocation(Titanium.MODID, "items/resource/nugget"));
        this.singleTexture("copper_ingot", mcLoc("item/generated"), "layer0", new ResourceLocation(Titanium.MODID, "items/resource/ingot"));
        this.singleTexture("silver_ingot", mcLoc("item/generated"), "layer0", new ResourceLocation(Titanium.MODID, "items/resource/ingot"));
        this.withExistingParent("copper_metal_block", modLoc(BLOCK_FOLDER + "/copper_metal_block"));
        this.withExistingParent("silver_metal_block", modLoc(BLOCK_FOLDER + "/silver_metal_block"));
    }

    @Override
    public String getName() {
        return "Workshop Resource Model Provider";
    }
}
