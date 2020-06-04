package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.tileentity.MoltenChamberTileEntity;

public class MoltenChamberBlock extends TileBlock<MoltenChamberTileEntity> {
    public MoltenChamberBlock() {
        super(Properties.from(Blocks.FURNACE).notSolid(), MoltenChamberTileEntity::new);
    }
}
