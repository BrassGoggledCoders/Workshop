package xyz.brassgoggledcoders.workshop.datagen.models;

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

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class WorkshopBlockstateProvider extends BlockStateProvider {

    private final ExistingFileHelper exFileHelper;

    public WorkshopBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Workshop.MOD_ID, exFileHelper);
        this.exFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<Block> block : WorkshopFluids.getAllBlocks()) {
            FlowingFluidBlock fluidBlock = (FlowingFluidBlock) block.get();
            Fluid fluid = fluidBlock.getFluid();
            if (fluid.isSource(fluid.getDefaultState())) {
                this.simpleBlock(block.get(), this.models()
                        .singleTexture(fluid.getRegistryName().getPath(), mcLoc("water"), "particle", mcLoc("block/water_still")));
            }
        }
        for (BlockRegistryObjectGroup<Block, BlockItem, ?> concrete : WorkshopBlocks.CONCRETES) {
            this.simpleBlock(concrete.getBlock(), this.models()
                    .cubeAll(concrete.getName(), new ResourceLocation("minecraft", BLOCK_FOLDER + "/" + concrete.getName().replace("_rebarred_", "_"))));
        }
        this.getVariantBuilder(WorkshopBlocks.OBSIDIAN_PLATE.get())
                .partialState().with(ObsidianPlateBlock.POWERED, true).addModels(
                new ConfiguredModel(models().withExistingParent("obsidian_plate_down", mcLoc("block/pressure_plate_down")).texture("texture", mcLoc("block/obsidian"))))
                .partialState().with(ObsidianPlateBlock.POWERED, false).addModels(
                new ConfiguredModel(models().withExistingParent("obsidian_plate_up", mcLoc("block/pressure_plate_up")).texture("texture", mcLoc("block/obsidian"))));
        this.directionalBlock(WorkshopBlocks.COLLECTOR.getBlock(),
                this.models().orientable("collector", mcLoc("block/furnace_top"), mcLoc("block/dropper_front_vertical"), mcLoc("block/furnace_top")));
        //this.horizontalBlock(WorkshopBlocks.BELLOWS.getBlock(),
        //        this.models().orientable("bellows", mcLoc("block/barrel_bottom"), modLoc("blocks/bellows"), mcLoc("block/composter_side")));
        this.horizontalBlock(WorkshopBlocks.SCRAP_BIN.getBlock(),
                this.models().orientable("scrap_bin", mcLoc("block/hopper_outside"), mcLoc("block/hopper_outside"), modLoc("blocks/scrap_bin_top")));
        this.simpleBlock(WorkshopBlocks.MOLTEN_CHAMBER.getBlock(), this.models().cubeAll("molten_chamber", modLoc("blocks/molten_chamber")));
        //this.simpleBlock(WorkshopBlocks.CHALK_WRITING.getBlock(), this.models().singleTexture("chalk", mcLoc(BLOCK_FOLDER + "/bedrock"), "particle", mcLoc(BLOCK_FOLDER + "/bedrock")));
        this.horizontalBlock(WorkshopBlocks.ALEMBIC.getBlock(), new ModelFile.ExistingModelFile(modLoc("block/alembic"), exFileHelper));
        this.logBlock(WorkshopBlocks.SEASONED_LOG.get());
        this.simpleBlock(WorkshopBlocks.DRYING_BASIN.getBlock(), this.models().withExistingParent(WorkshopBlocks.DRYING_BASIN.getName(), mcLoc(BLOCK_FOLDER + "/cauldron"))
                .texture("side", modLoc(BLOCK_FOLDER + "s/drying_basin_side"))
                .texture("top", modLoc(BLOCK_FOLDER + "s/drying_basin_top"))
                .texture("bottom", modLoc(BLOCK_FOLDER + "s/drying_basin_bottom"))
                .texture("inside", mcLoc(BLOCK_FOLDER + "/polished_andesite")));
    }
}
