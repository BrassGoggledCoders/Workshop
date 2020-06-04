package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.tileentity.CollectorTileEntity;

public class CollectorBlock extends TileBlock<CollectorTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public CollectorBlock() {
        super(Properties.from(Blocks.FURNACE), CollectorTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(worldIn.getTileEntity(pos) instanceof CollectorTileEntity) {
            this.handleTileEntity(worldIn, pos, tile -> ((CollectorTileEntity) tile).getMachineComponent().forceRecipeRecheck());
        }
    }
}
