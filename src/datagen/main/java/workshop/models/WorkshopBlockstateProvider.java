package workshop.models;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SixWayBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.ItemductBlock;
import xyz.brassgoggledcoders.workshop.block.ObsidianPlateBlock;
import xyz.brassgoggledcoders.workshop.block.SinteringFurnaceBlock;
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
                this.models().withExistingParent("collector", mcLoc("block/hopper"))
                        .texture("side", mcLoc("block/stone"))
                        .texture("inside", mcLoc("block/stone"))
                        .texture("top", modLoc("block/drying_basin_top")));
        this.horizontalBlock(WorkshopBlocks.SCRAP_BIN.getBlock(),
                this.models().orientable("scrap_bin", mcLoc("block/hopper_outside"), mcLoc("block/hopper_outside"), modLoc("block/scrap_bin_top")));
        this.simpleBlock(WorkshopBlocks.MOLTEN_CHAMBER.getBlock(), this.models().cubeAll("molten_chamber", modLoc("block/molten_chamber")));
        this.horizontalBlock(WorkshopBlocks.ALEMBIC.getBlock(), new ModelFile.ExistingModelFile(modLoc("block/alembic"), exFileHelper));
        //TODO De pluralise texture folders
        this.logBlock(WorkshopBlocks.SEASONED_LOG.get());
        this.logBlock(WorkshopBlocks.STRIPPED_SEASONED_LOG.getBlock());
        this.simpleBlock(WorkshopBlocks.SILO_BARREL.get(), this.models().cubeBottomTop(WorkshopBlocks.SILO_BARREL.getName(), mcLoc(BLOCK_FOLDER + "/barrel_side"),
                mcLoc(BLOCK_FOLDER + "/hopper_inside"), mcLoc(BLOCK_FOLDER + "/barrel_top")));
        //section Itemduct
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(WorkshopBlocks.ITEMDUCT.get())
                .part().modelFile(models()
                        .withExistingParent("itemduct_center", modLoc("template_duct_center"))
                        .texture("main", modLoc("block/stripped_seasoned_log")
                        ).texture("particle", modLoc("block/stripped_seasoned_log"))).addModel()
                .end();
        BlockModelBuilder side = models().withExistingParent("itemduct_side", modLoc("template_duct_side")).texture("main", modLoc("block/stripped_seasoned_log")
        ).texture("particle", modLoc("block/stripped_seasoned_log"));
        BlockModelBuilder oside = models().withExistingParent("itemduct_side_output", modLoc("template_duct_side")).texture("main", mcLoc("block/hopper_inside")
        ).texture("particle", mcLoc("block/hopper_inside"));
        SixWayBlock.FACING_TO_PROPERTY_MAP.forEach((dir, value) -> {
            switch (dir) {
                case UP:
                    builder.part().modelFile(side).rotationX(-90).uvLock(true).addModel().condition(value, true);
                    builder.part().modelFile(oside).rotationX(-90).uvLock(true).addModel().condition(ItemductBlock.FACING, dir);
                    break;
                case DOWN:
                    builder.part().modelFile(side).rotationX(90).uvLock(true).addModel().condition(value, true);
                    builder.part().modelFile(oside).rotationX(90).uvLock(true).addModel().condition(ItemductBlock.FACING, dir);
                    break;
                default:
                    builder.part().modelFile(side).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).addModel()
                            .condition(value, true);
                    builder.part().modelFile(oside).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).addModel()
                            .condition(ItemductBlock.FACING, dir);
                    break;
            }
        });
        //endsection
        //section Sintering
        ResourceLocation location = modLoc("block/sintering_furnace");
        ModelFile sintering_furnace = new ModelFile.ExistingModelFile(location, exFileHelper);
        ModelFile sintering_furnace_hot = models().withExistingParent("sintering_furnace_hot", location).texture("0", modLoc(BLOCK_FOLDER + "/hot_iron_block"));
        this.getVariantBuilder(WorkshopBlocks.SINTERING_FURNACE.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.get(SinteringFurnaceBlock.LIT) ? sintering_furnace_hot : sintering_furnace)
                .rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle() + 180) % 360)
                .build());
        //endsection
        this.horizontalBlock(WorkshopBlocks.PRESS.getBlock(), new ModelFile.ExistingModelFile(modLoc("block/press"), exFileHelper));
        this.simpleBlock(WorkshopBlocks.SILT.getBlock());
        this.simpleBlock(WorkshopBlocks.SILTSTONE.getBlock(), this.models().cubeBottomTop("siltstone", modLoc("block/siltstone"), modLoc("block/siltstone_bottom"), modLoc("block/siltstone_top")));
    }
}
