package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

@SuppressWarnings("unused")
public class WorkshopItems {

    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new Item(new Item.Properties()
            .group(Workshop.ITEM_GROUP))
    );

    public static final RegistryObject<Item> CARAMEL_APPLE = ITEMS.register("caramel_apple",
            () -> new Item(new Item.Properties()
                    .group(Workshop.ITEM_GROUP)
                    .food(Foods.APPLE)
            )
    );

    public static final RegistryObject<Item> ROSIN = ITEMS.register("rosin", () -> new Item(new Item.Properties().group(Workshop.ITEM_GROUP)));

    public static final RegistryObject<Item> ASH = ITEMS.register("ash", () -> new Item(new Item.Properties().group(Workshop.ITEM_GROUP)));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
