package xyz.brassgoggledcoders.workshop.item;


import net.minecraft.item.Item;
import xyz.brassgoggledcoders.workshop.util.WorkGroup;


public class ItemProperties
{

    public static Item.Properties ICON = builder();
    public static Item.Properties ITEM = builder().group(WorkGroup.instance);



    private static Item.Properties builder() {

        return new Item.Properties();
    }
}
