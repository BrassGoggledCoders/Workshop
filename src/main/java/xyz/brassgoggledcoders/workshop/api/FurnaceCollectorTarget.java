package xyz.brassgoggledcoders.workshop.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.ArrayUtils;

public class FurnaceCollectorTarget implements ICollectorTarget {

    FurnaceTileEntity tile;

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
