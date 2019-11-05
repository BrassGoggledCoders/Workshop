package xyz.brassgoggledcoders.workshop.blocks.alembic;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AlembicBlock extends BlockTileBase {

    public AlembicBlock() {
        super("alembic", Properties.from(Blocks.IRON_BLOCK), AlembicTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return AlembicTile::new;
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
