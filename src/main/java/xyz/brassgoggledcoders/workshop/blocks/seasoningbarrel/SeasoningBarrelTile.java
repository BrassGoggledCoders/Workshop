package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import static xyz.brassgoggledcoders.workshop.registries.TileEntities.SEASONING_BARREL_TILE;

public class SeasoningBarrelTile extends TileEntity implements ITickableTileEntity {

    public SeasoningBarrelTile() {
        super(SEASONING_BARREL_TILE.get());
    }

    @Override
    public void tick() {

    }

}
