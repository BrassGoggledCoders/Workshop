package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;
import xyz.brassgoggledcoders.workshop.tileentity.MortarTileEntity;

public class MortarBlock extends TileBlock {

    public MortarBlock() {
        super(Properties.from(Blocks.GRANITE), MortarTileEntity::new);
    }
}
