package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Blocks;
import xyz.brassgoggledcoders.workshop.tileentity.DryingBasinTileEntity;

public class DryingBasinBlock extends GUITileBlock<DryingBasinTileEntity> {
    public DryingBasinBlock() {
        super(Properties.from(Blocks.CHEST).notSolid(), DryingBasinTileEntity::new);
    }
}