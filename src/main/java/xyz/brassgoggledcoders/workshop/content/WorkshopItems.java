package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.item.BottleItem;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

@SuppressWarnings("unused")
public class WorkshopItems {

    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    //region Ingredients
    public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new Item(new Item.Properties().group(Workshop.ITEM_GROUP)));
    public static final RegistryObject<Item> ROSIN = ITEMS.register("rosin", () -> new Item(new Item.Properties().group(Workshop.ITEM_GROUP)));
    public static final RegistryObject<Item> ASH = ITEMS.register("ash", () -> new Item(new Item.Properties().group(Workshop.ITEM_GROUP)));
    public static final RegistryObject<Item> TALLOW = ITEMS.register("tallow", () -> new Item(new Item.Properties().group(Workshop.ITEM_GROUP)));
    //endregion

    //region Foods
    public static final RegistryObject<Item> PICKLE = ITEMS.register("pickle", () -> new Item(new Item.Properties()
            .group(Workshop.ITEM_GROUP)
            .food(Foods.CARROT)));
    public static final RegistryObject<Item> CARAMEL_APPLE = ITEMS.register("caramel_apple", () -> new Item(new Item.Properties()
                .group(Workshop.ITEM_GROUP)
                .food(Foods.APPLE)));
    public static final RegistryObject<Item> TEA_LEAVES = ITEMS.register("tea_leaves", () -> new Item(new Item.Properties()
            .group(Workshop.ITEM_GROUP)));
    //endregion

    //region Fluid Items
    public static final Map<FluidRegistryObjectGroup<?, ?>, RegistryObject<BottleItem>> BOTTLES = Stream.of(WorkshopFluids.ADHESIVE_OILS, WorkshopFluids.CIDER, WorkshopFluids.TEA)
            .map(fluid -> Pair.of(fluid, ITEMS.register(fluid.getName() + "_bottle", () -> new BottleItem(fluid, new Item.Properties()
                            .group(Workshop.ITEM_GROUP)))))
            //Can't get the fluid from the stack because the stack isn't registered yet
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    //endregion

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

}
