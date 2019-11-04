package xyz.brassgoggledcoders.workshop.blocks.alembic;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import static xyz.brassgoggledcoders.workshop.registries.TileEntities.ALEMBICTILE;

public class AlembicTile extends TileEntity implements ITickableTileEntity {

    public AlembicTile() {
        super(ALEMBICTILE.get());
    }

    @Override
    public void tick() {

    }

}
