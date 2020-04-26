package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.tileentity.SpinningWheelTileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class SpinningWheelBlock extends TileBlock<SpinningWheelTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SpinningWheelBlock() {
        super(Properties.from(Blocks.OAK_PLANKS).notSolid(), SpinningWheelTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        handleTileEntity(worldIn, pos, tile -> {
            if (tile instanceof SpinningWheelTileEntity) {
                ((SpinningWheelTileEntity) tile).setTimes(2 * 20, 2 * 20);
            }
        });
    }
}
