package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

public class TeaPlantBlock extends CropsBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_1;

    public TeaPlantBlock() {
        super(Properties.from(Blocks.WHEAT));
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return WorkshopBlocks.TEA_PLANT.getItem();
    }

    @Override
    public int getMaxAge() {
        return 1;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
