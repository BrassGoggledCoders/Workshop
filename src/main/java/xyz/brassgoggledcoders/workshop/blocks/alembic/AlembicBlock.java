package xyz.brassgoggledcoders.workshop.blocks.alembic;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.block.Blocks;

public class AlembicBlock extends BasicTileBlock<AlembicTile> {

    public AlembicBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), AlembicTile.class);
    }

    @Override
    public IFactory<AlembicTile> getTileEntityFactory() {
        return AlembicTile::new;
    }
}
