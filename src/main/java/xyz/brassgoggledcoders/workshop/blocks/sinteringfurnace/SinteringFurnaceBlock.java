package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

//TODO: Return to using Titanium base class when it has the right constructor
public class SinteringFurnaceBlock extends BasicTileBlock<SinteringFurnaceTile> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SinteringFurnaceBlock() {
        super(Properties.from(Blocks.FURNACE), SinteringFurnaceTile.class);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public IFactory<SinteringFurnaceTile> getTileEntityFactory() {
        return SinteringFurnaceTile::new;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }
}
