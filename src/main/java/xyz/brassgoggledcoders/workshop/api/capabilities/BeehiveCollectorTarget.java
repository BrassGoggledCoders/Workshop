package xyz.brassgoggledcoders.workshop.api.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.BeehiveTileEntity;

public class BeehiveCollectorTarget implements CollectorTarget {

    final BeehiveTileEntity tile;

    public BeehiveCollectorTarget(BeehiveTileEntity tile) {
        this.tile = tile;
    }

    @Override
    public ItemStack[] getCollectables() {
        //TODO
        return new ItemStack[]{ new ItemStack(Items.BEDROCK) };
    }

    @Override
    public boolean isActive() {
        return tile.isFullOfBees();
    }
}
