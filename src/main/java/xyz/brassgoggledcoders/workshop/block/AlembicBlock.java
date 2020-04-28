package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;

public class AlembicBlock extends TileBlock<AlembicTileEntity> {

    public AlembicBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), AlembicTileEntity::new);
    }
}
