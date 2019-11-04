package xyz.brassgoggledcoders.workshop.registries;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.util.WorkGroup;

import static xyz.brassgoggledcoders.workshop.WorkShop.MOD_ID;

public class Items {

    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> WORKICON = ITEMS.register("workicon", () -> new Item(new Item.Properties()));

    //Alembic Outputs
    public static final RegistryObject<Item> ADHESIVE_OILITEM = ITEMS.register("adhesive_oil", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> DISTILLED_WATERITEM = ITEMS.register("distilled_water", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> TANINITEM = ITEMS.register("tanin", () -> new Item(new Item.Properties().group(WorkGroup.instance)));

    //Ingredients
    public static final RegistryObject<Item> SALTITEM = ITEMS.register("salt", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> CHALKITEM = ITEMS.register("chalk", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> ASHITEM = ITEMS.register("ash", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> SILTITEM = ITEMS.register("silt", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> TEAITEM = ITEMS.register("tea", () -> new Item(new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> MEDICINAL_ROOTSITEM = ITEMS.register("medicinalroots", () -> new Item(new Item.Properties().group(WorkGroup.instance)));



    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
