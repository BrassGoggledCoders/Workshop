package xyz.brassgoggledcoders.workshop.capabilities;

import net.minecraft.item.ItemStack;

public class DefaultCollectorTarget implements ICollectorTarget {
    @Override
    public ItemStack[] getCollectables() {
        return new ItemStack[0];
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
