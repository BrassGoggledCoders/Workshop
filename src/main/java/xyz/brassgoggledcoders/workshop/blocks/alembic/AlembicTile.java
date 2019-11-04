package xyz.brassgoggledcoders.workshop.blocks.alembic;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import static xyz.brassgoggledcoders.workshop.registries.TileEntities.ALEMBIC_TILE;

public class AlembicTile extends TileEntity implements ITickableTileEntity {

    public AlembicTile() {
        super(ALEMBIC_TILE.get());
    }

    @Override
    public void tick() {

    }

}
