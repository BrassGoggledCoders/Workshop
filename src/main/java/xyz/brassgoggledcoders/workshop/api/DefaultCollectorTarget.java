package xyz.brassgoggledcoders.workshop.api;

import net.minecraft.item.ItemStack;

public class DefaultCollectorTarget implements ICollectorTarget {
    @Override
    public ItemStack[] getCollectables() {
        return new ItemStack[0];
    }
}
