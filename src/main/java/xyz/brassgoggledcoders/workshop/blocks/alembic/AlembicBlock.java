package xyz.brassgoggledcoders.workshop.blocks.alembic;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.Blocks;

public class AlembicBlock extends BlockTileBase {

    public AlembicBlock() {
        super("alembic", Properties.from(Blocks.IRON_BLOCK), AlembicTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return AlembicTile::new;
    }
}
