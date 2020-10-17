package xyz.brassgoggledcoders.workshop.datagen.models;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.ObsidianPlateBlock;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopResourcePlugin;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class WorkshopResourceBlockstateProvider extends BlockStateProvider {

    public WorkshopResourceBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Titanium.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(WorkshopResourcePlugin.COPPER_BLOCK);
        this.simpleBlock(WorkshopResourcePlugin.SILVER_BLOCK);
    }
}
