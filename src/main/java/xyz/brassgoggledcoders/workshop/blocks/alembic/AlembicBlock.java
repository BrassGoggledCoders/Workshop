package xyz.brassgoggledcoders.workshop.blocks.alembic;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;

import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.Workshop;

public class AlembicBlock extends BlockTileBase<AlembicTile> {

    public AlembicBlock() {
        super("alembic", Properties.from(Blocks.IRON_BLOCK), AlembicTile.class);
        setItemGroup(Workshop.workshopTab);
    }

    @Override
    public IFactory<AlembicTile> getTileEntityFactory() {
        return AlembicTile::new;
    }
}
