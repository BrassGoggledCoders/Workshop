package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicTile;

import javax.annotation.Nullable;

public class SpinningWheelBlock extends Block {

    public SpinningWheelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SpinningWheelTile()  ;
    }

}
