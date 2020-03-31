package xyz.brassgoggledcoders.workshop.datagen.models;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.BlockRegistryObjectGroup;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

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
        for(RegistryObject<Block> block : WorkshopFluids.getAllBlocks()) {
            FlowingFluidBlock fluidBlock = (FlowingFluidBlock) block.get();
            Fluid fluid = fluidBlock.getFluid();
            if(fluid.isSource(fluid.getDefaultState())) {
                this.simpleBlock(block.get(), new ModelFile.ExistingModelFile(modLoc(BLOCK_FOLDER + "/" + fluid.getRegistryName().getPath()), helper));
            }
        }
        for(BlockRegistryObjectGroup concrete : WorkshopBlocks.CONCRETES) {
            this.simpleBlock(concrete.getBlock(), new ModelFile.ExistingModelFile(modLoc(BLOCK_FOLDER + "/" + concrete.getName()), helper));
        }
    }
}
