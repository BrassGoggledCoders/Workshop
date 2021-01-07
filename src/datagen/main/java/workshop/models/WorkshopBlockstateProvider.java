package workshop.models;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
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
                        .singleTexture(fluidBlock.getRegistryName().getPath(), mcLoc("water"), "particle", mcLoc("block/water_still")));
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
        //        this.models().orientable("bellows", mcLoc("block/barrel_bottom"), modLoc("block/bellows"), mcLoc("block/composter_side")));
        this.horizontalBlock(WorkshopBlocks.SCRAP_BIN.getBlock(),
                this.models().orientable("scrap_bin", mcLoc("block/hopper_outside"), mcLoc("block/hopper_outside"), modLoc("block/scrap_bin_top")));
        this.simpleBlock(WorkshopBlocks.MOLTEN_CHAMBER.getBlock(), this.models().cubeAll("molten_chamber", modLoc("block/molten_chamber")));
        //this.simpleBlock(WorkshopBlocks.CHALK_WRITING.getBlock(), this.models().singleTexture("chalk", mcLoc(BLOCK_FOLDER + "/bedrock"), "particle", mcLoc(BLOCK_FOLDER + "/bedrock")));
        this.horizontalBlock(WorkshopBlocks.ALEMBIC.getBlock(), new ModelFile.ExistingModelFile(modLoc("block/alembic"), exFileHelper));
        //TODO De pluralise texture folders
        //this.logBlock(WorkshopBlocks.SEASONED_LOG.get());
        //TODO Inventory rotations
        /*this.simpleBlock(WorkshopBlocks.DRYING_BASIN.getBlock(), this.models().withExistingParent(WorkshopBlocks.DRYING_BASIN.getName(), mcLoc(BLOCK_FOLDER + "/cauldron"))
                .texture("side", modLoc(BLOCK_FOLDER + "s/drying_basin_side"))
                .texture("top", modLoc(BLOCK_FOLDER + "s/drying_basin_top"))
                .texture("bottom", modLoc(BLOCK_FOLDER + "s/drying_basin_bottom"))
                .texture("inside", mcLoc(BLOCK_FOLDER + "/polished_andesite")));
        this.simpleBlock(WorkshopBlocks.FLUID_FUNNEL.get(), this.models().withExistingParent(WorkshopBlocks.FLUID_FUNNEL.getName(), mcLoc(BLOCK_FOLDER + "/hopper"))
                .texture("side", modLoc(BLOCK_FOLDER + "s/seasoned_log"))
                .texture("inside", modLoc(BLOCK_FOLDER + "s/seasoned_log"))
                .texture("top", modLoc(BLOCK_FOLDER + "s/fluid_funnel_top")));*/
        this.simpleBlock(WorkshopBlocks.SILO_BARREL.get(), this.models().cubeBottomTop(WorkshopBlocks.SILO_BARREL.getName(), mcLoc(BLOCK_FOLDER + "/barrel_side"),
                mcLoc(BLOCK_FOLDER + "/hopper_inside"), mcLoc(BLOCK_FOLDER + "/barrel_top")));
    }
}
