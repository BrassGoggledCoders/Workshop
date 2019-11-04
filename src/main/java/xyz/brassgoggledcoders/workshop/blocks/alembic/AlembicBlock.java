package xyz.brassgoggledcoders.workshop.blocks.alembic;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.workshop.blocks.BlockBase;

import javax.annotation.Nullable;

public class AlembicBlock extends BlockBase {

    public AlembicBlock(Properties properties, String id) {
        super(properties, id);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AlembicTile();
    }

}
