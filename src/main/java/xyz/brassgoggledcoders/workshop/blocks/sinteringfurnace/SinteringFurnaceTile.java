package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import static xyz.brassgoggledcoders.workshop.registries.TileEntities.SINTERING_FURNACE_TILE;

public class SinteringFurnaceTile extends TileEntity implements ITickableTileEntity {

    public SinteringFurnaceTile() {
        super(SINTERING_FURNACE_TILE.get());
    }

    @Override
    public void tick() {

    }

}
