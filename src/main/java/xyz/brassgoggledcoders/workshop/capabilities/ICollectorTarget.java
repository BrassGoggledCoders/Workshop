package xyz.brassgoggledcoders.workshop.capabilities;

import net.minecraft.item.ItemStack;

public interface ICollectorTarget {
    ItemStack[] getCollectables();

    boolean isActive();
}
