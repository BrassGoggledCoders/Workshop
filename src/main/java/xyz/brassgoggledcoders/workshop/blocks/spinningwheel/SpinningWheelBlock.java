package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class SpinningWheelBlock extends BasicTileBlock<SpinningWheelTile> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SpinningWheelBlock() {
        super(Properties.from(Blocks.OAK_PLANKS).notSolid(), SpinningWheelTile.class);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public IFactory<SpinningWheelTile> getTileEntityFactory() {
        return SpinningWheelTile::new;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }
}
