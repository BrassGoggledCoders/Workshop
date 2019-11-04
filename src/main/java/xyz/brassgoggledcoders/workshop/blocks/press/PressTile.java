package xyz.brassgoggledcoders.workshop.blocks.press;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import static xyz.brassgoggledcoders.workshop.registries.TileEntities.PRESS_TILE;

public class PressTile extends TileEntity implements ITickableTileEntity {

    public PressTile() {
        super(PRESS_TILE.get());
    }

    @Override
    public void tick() {

    }

}
