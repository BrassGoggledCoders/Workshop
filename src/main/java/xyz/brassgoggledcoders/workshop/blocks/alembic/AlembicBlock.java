package xyz.brassgoggledcoders.workshop.blocks.alembic;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.blocks.TileBlock;

public class AlembicBlock extends TileBlock {

    public AlembicBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), AlembicTile::new);
    }

}
