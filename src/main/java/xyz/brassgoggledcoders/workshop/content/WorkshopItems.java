package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.item.BottleItem;
import xyz.brassgoggledcoders.workshop.item.ScrapBagItem;
import xyz.brassgoggledcoders.workshop.item.SoapItem;

import java.util.Map;
import java.util.stream.Collectors;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

@SuppressWarnings("unused")
public class WorkshopItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    //region Ingredients
    public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> ROSIN = ITEMS.register("rosin", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> ASH = ITEMS.register("ash", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> TALLOW = ITEMS.register("tallow", () -> new Item(defaultProperties()));
    //public static final RegistryObject<Item> MEDICINAL_ROOT = ITEMS.register("medicinal_root", () -> new Item(defaultProperties().food(Foods.CARROT)));
    public static final RegistryObject<Item> TANNIN = ITEMS.register("tannin", () -> new Item(defaultProperties()));
    //public static final RegistryObject<Item> SILT = ITEMS.register("silt", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> LEATHER_CORDAGE = ITEMS.register("leather_cordage", () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> LYE = ITEMS.register("lye", () -> new Item(defaultProperties()));
    //endregion

    public static final RegistryObject<Item> SOAP = ITEMS.register("soap", () -> new SoapItem(defaultProperties()));

    //region Foods
    public static final RegistryObject<Item> PICKLE = ITEMS.register("pickle", () -> new Item(defaultProperties()
            .food(Foods.CARROT)));
    public static final RegistryObject<Item> CARAMEL_APPLE = ITEMS.register("caramel_apple", () -> new Item(defaultProperties()
            .food(Foods.APPLE)));
    public static final RegistryObject<Item> TEA_LEAVES = ITEMS.register("tea_leaves", () -> new Item(defaultProperties()));
    //endregion

    //region Fluid Items
    public static final Map<ResourceLocation, RegistryObject<BottleItem>> BOTTLES = WorkshopFluids.getAllFluids().stream()
            .filter(fluid -> !(fluid.getId().getPath().contains("honey"))) //Exists in vanilla
            .map(fluid -> Pair.of(fluid.getId(), ITEMS.register(fluid.getId().getPath() + "_bottle", () -> new BottleItem(fluid, defaultProperties()))))
            //Can't get the fluid from the stack because the stack isn't registered yet
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    //endregion

    public static final RegistryObject<Item> SCRAP_BAG = ITEMS.register("scrap_bag", () -> new ScrapBagItem(defaultProperties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private static Item.Properties defaultProperties() {
        return new Item.Properties().group(Workshop.ITEM_GROUP);
    }
}
