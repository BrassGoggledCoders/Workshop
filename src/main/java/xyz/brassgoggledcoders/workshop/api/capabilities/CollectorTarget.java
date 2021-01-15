package xyz.brassgoggledcoders.workshop.api.capabilities;

import net.minecraft.item.ItemStack;

public interface CollectorTarget {
    ItemStack[] getCollectables();

    boolean isActive();
}
