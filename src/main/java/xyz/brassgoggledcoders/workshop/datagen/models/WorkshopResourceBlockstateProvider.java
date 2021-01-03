package xyz.brassgoggledcoders.workshop.datagen.models;

import com.hrznstudio.titanium.Titanium;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.brassgoggledcoders.workshop.content.WorkshopResourcePlugin;

public class WorkshopResourceBlockstateProvider extends BlockStateProvider {

    public WorkshopResourceBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Titanium.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //this.simpleBlock(WorkshopResourcePlugin.COPPER_BLOCK, this.models().cubeAll("copper_metal_block", new ResourceLocation(Titanium.MODID, "blocks/resource/block")));
        //this.simpleBlock(WorkshopResourcePlugin.SILVER_BLOCK, this.models().cubeAll("silver_metal_block", new ResourceLocation(Titanium.MODID, "blocks/resource/block")));
    }
}
