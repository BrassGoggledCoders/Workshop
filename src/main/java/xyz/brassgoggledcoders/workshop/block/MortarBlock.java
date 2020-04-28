package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.tileentity.MortarTileEntity;

public class MortarBlock extends TileBlock<MortarTileEntity> {

    public MortarBlock() {
        super(Properties.from(Blocks.GRANITE), MortarTileEntity::new);
    }
}
