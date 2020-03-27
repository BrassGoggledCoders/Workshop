package xyz.brassgoggledcoders.workshop.blocks.alembic;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.Workshop;

public class AlembicBlock extends BasicTileBlock<AlembicTile> {

    public AlembicBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), AlembicTile.class);
        this.setItemGroup(Workshop.ITEM_GROUP);
    }

    @Override
    public IFactory<AlembicTile> getTileEntityFactory() {
        return AlembicTile::new;
    }
}
