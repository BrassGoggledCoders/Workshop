package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import com.sun.org.apache.regexp.internal.RE;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.recipe.*;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class WorkshopRecipes {

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final RegistryObject<GenericSerializer<SpinningWheelRecipe>> SPINNING_WHEEL_SERIALIZER = serializer("spinning_wheel", SpinningWheelRecipe.class);
    public static final RegistryObject<GenericSerializer<PressRecipe>> PRESS_SERIALIZER = serializer("press", PressRecipe.class);
    public static final RegistryObject<GenericSerializer<AlembicRecipe>> ALEMBIC_SERIALIZER = serializer("alembic", AlembicRecipe.class);
    public static final RegistryObject<GenericSerializer<SeasoningBarrelRecipe>> SEASONING_BARREL_SERIALIZER = serializer("seasoning_barrel", SeasoningBarrelRecipe.class);
    public static final RegistryObject<GenericSerializer<SinteringFurnaceRecipe>> SINTERING_FURNACE_SERIALIZER = serializer("sintering_furnace", SinteringFurnaceRecipe.class);
    public static final RegistryObject<GenericSerializer<CollectorRecipe>> COLLECTOR_SERIALIZER = serializer("collector", CollectorRecipe.class);
    public static final RegistryObject<GenericSerializer<MoltenChamberRecipe>> MOLTEN_CHAMBER_SERIALIZER = serializer("molten_chamber", MoltenChamberRecipe.class);
    public static final RegistryObject<GenericSerializer<MortarRecipe>> MORTAR_SERIALIZER = serializer("mortar", MortarRecipe.class);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZER.register(bus);
    }

    public static <R extends SerializableRecipe> RegistryObject<GenericSerializer<R>> serializer(String name, Class<R> recipeClass) {
        return RECIPE_SERIALIZER.register(name,
                () -> new GenericSerializer<>(IRecipeType.register(MOD_ID + ":" + name), recipeClass));
    }
}
