package xyz.brassgoggledcoders.workshop.blocks;

import com.hrznstudio.titanium.module.Feature;
import net.minecraft.block.Block;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicBlock;
import xyz.brassgoggledcoders.workshop.blocks.press.PressBlock;
import xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel.SeasoningBarrelBlock;
import xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace.SinteringFurnaceBlock;
import xyz.brassgoggledcoders.workshop.blocks.spinningwheel.SpinningWheelBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockNames{

    public static AlembicBlock ALEMBIC_BLOCK = new AlembicBlock();
    public static PressBlock PRESS_BLOCK = new PressBlock();
    public static SinteringFurnaceBlock SINTERING_FURNACE_BLOCK = new SinteringFurnaceBlock();
    public static SpinningWheelBlock SPINNING_WHEEL_BLOCK = new SpinningWheelBlock();
    public static SeasoningBarrelBlock SEASONING_BARREL_BLOCK = new SeasoningBarrelBlock();


    public List<Feature.Builder> generateFeatures() {
        List<Feature.Builder> features = new ArrayList<>();
        features.add(Feature.builder("alembic").
                content(Block.class, ALEMBIC_BLOCK));
        features.add(Feature.builder("press").
                content(Block.class, PRESS_BLOCK));
        features.add(Feature.builder("sintering_furnace").
                content(Block.class, SINTERING_FURNACE_BLOCK));
        features.add(Feature.builder("spinning_wheel").
                content(Block.class, SPINNING_WHEEL_BLOCK));
        features.add(Feature.builder("seasoning_barrel").
                content(Block.class, SEASONING_BARREL_BLOCK));
        return features;
    }

}
