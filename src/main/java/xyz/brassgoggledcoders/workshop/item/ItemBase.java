package xyz.brassgoggledcoders.workshop.item;

import net.minecraft.item.Item;

public class ItemBase extends Item {
    public ItemBase(Properties properties, String id) {
        super(properties);
        setRegistryName(id);
    }


}
