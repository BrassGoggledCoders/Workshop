package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.tileentity.MoltenChamberTileEntity;

public class MoltenChamberBlock extends GUITileBlock<MoltenChamberTileEntity> {
    public MoltenChamberBlock() {
        super(Properties.from(Blocks.STONE).notSolid(), MoltenChamberTileEntity::new);
    }
}
