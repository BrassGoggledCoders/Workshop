package xyz.brassgoggledcoders.workshop.api;

import net.minecraft.item.ItemStack;

public interface ICollectorTarget {
    ItemStack[] getCollectables();
}
