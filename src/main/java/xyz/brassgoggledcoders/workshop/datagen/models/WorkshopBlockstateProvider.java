package xyz.brassgoggledcoders.workshop.datagen.models;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SixWayBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Direction;
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

        ModelFile pipeArm = this.models().getBuilder("block/pipe/arm")
                .element()
                .face(Direction.NORTH)
                .texture("workshop:blocks/glass_pipe")
                .uvs(0, 10, 4, 16)
                .end()
                .end();

        ModelFile pipeCenter = this.models().getBuilder("block/pipe/center")
                .element()
                .face(Direction.UP)
                .texture("workshop:blocks/glass_pipe")
                .uvs(0, 0, 4, 4)
                .end()
                .end();

        this.getMultipartBuilder(WorkshopBlocks.PIPE.getBlock())
                .part().modelFile(pipeCenter).addModel().condition(SixWayBlock.UP, false).end()
                .part().modelFile(pipeArm).addModel().condition(SixWayBlock.UP, true).end()
                .part().modelFile(pipeCenter).rotationX(180).addModel().condition(SixWayBlock.DOWN, false).end()
                .part().modelFile(pipeArm).rotationX(180).addModel().condition(SixWayBlock.DOWN, true).end()
                .part().modelFile(pipeCenter).rotationX(90).addModel().condition(SixWayBlock.NORTH, false).end()
                .part().modelFile(pipeArm).rotationX(90).addModel().condition(SixWayBlock.NORTH, true).end()
                .part().modelFile(pipeCenter).rotationX(90).rotationY(90).addModel().condition(SixWayBlock.EAST, false).end()
                .part().modelFile(pipeArm).rotationX(90).rotationY(90).addModel().condition(SixWayBlock.EAST, true).end()
                .part().modelFile(pipeCenter).rotationX(90).rotationY(180).addModel().condition(SixWayBlock.SOUTH, false).end()
                .part().modelFile(pipeArm).rotationX(90).rotationY(180).addModel().condition(SixWayBlock.SOUTH, true).end()
                .part().modelFile(pipeCenter).rotationX(90).rotationY(270).addModel().condition(SixWayBlock.WEST, false).end()
                .part().modelFile(pipeArm).rotationX(90).rotationY(270).addModel().condition(SixWayBlock.WEST, true).end();

        this.directionalBlock(WorkshopBlocks.PUMP.getBlock(), this.models().orientable("pump",
                mcLoc("block/furnace_top"), mcLoc("block/dropper_front_vertical"),
                mcLoc("block/furnace_top")));
    }
}
