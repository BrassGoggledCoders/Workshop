package xyz.brassgoggledcoders.workshop.api.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.FurnaceTileEntity;
import org.apache.commons.lang3.ArrayUtils;

public class FurnaceCollectorTarget implements CollectorTarget {

    final FurnaceTileEntity tile;

    public FurnaceCollectorTarget(FurnaceTileEntity tile) {
        this.tile = tile;
    }

    @Override
    public ItemStack[] getCollectables() {
        //Both fuel and input
        return ArrayUtils.addAll(new ItemStack[2], tile.getStackInSlot(0), tile.getStackInSlot(1));
    }

    @Override
    public boolean isActive() {
        return tile.isBurning();
    }
}
