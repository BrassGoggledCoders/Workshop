package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;
import xyz.brassgoggledcoders.workshop.tileentity.SealedBarrelTileEntity;

public class SealedBarrelBlock extends TileBlock<SealedBarrelTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public SealedBarrelBlock() {
        super(Properties.from(Blocks.OAK_LOG), SealedBarrelTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }
}
