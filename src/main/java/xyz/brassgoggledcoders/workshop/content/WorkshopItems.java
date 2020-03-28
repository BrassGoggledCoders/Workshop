package xyz.brassgoggledcoders.workshop.content;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;

@SuppressWarnings("UNUSED")
public class WorkshopItems {

    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);
    //public static final RegistryObject<Item> WORKICON = ITEMS.register("workicon", () -> new Item(new Item.Properties()));

/*
    //Alembic Outputs
    public static final RegistryObject<Item> ADHESIVE_OIL = ITEMS.register("adhesive_oil", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> DISTILLED_WATER = ITEMS.register("distilled_water", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> TANNIN = ITEMS.register("tannin", () -> new Item(new Item.Properties().group(WorkGroup.instance)));

    //Ingredients
    public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> CHALK = ITEMS.register("chalk", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> ASH = ITEMS.register("ash", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> SILT = ITEMS.register("silt", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> TEA = ITEMS.register("tea", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> MEDICINAL_ROOTS = ITEMS.register("medicinal_roots", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> RESIN_BUCKET = ITEMS.register("resin_bucket", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> METAL_DUST = ITEMS.register("stranglegrass", () -> new Item(new Item.Properties().group(WorkGroup.instance)));


*/

    public static final RegistryObject<Item> CARAMEL_APPLE = ITEMS.register("caramel_apple",
            () -> new Item(new Item.Properties()
                    .group(Workshop.ITEM_GROUP)
                    .food(Items.APPLE.getFood())));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
