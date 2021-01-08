package xyz.brassgoggledcoders.workshop.api.capabilities;

import net.minecraft.item.ItemStack;

public interface ICollectorTarget {
    ItemStack[] getCollectables();

    boolean isActive();
}
