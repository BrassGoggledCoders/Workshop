package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import static xyz.brassgoggledcoders.workshop.registries.TileEntities.SPINNING_WHEEL_TILE;

public class SpinningWheelTile extends TileEntity implements ITickableTileEntity {

    public SpinningWheelTile() {
        super(SPINNING_WHEEL_TILE.get());
    }

    @Override
    public void tick() {

    }

}
