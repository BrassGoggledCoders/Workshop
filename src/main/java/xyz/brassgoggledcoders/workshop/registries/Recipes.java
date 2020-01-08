package xyz.brassgoggledcoders.workshop.registries;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.recipes.*;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class Recipes {

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final IRecipeType<SpinningWheelRecipe> SPINNING_WHEEL = IRecipeType.register(MOD_ID + "spinning_wheel");
    public static final RegistryObject<GenericSerializer<SpinningWheelRecipe>> SPINNING_WHEEL_SERIALIZER = RECIPE_SERIALIZER.register("spinning_wheel",
            () -> new GenericSerializer<>(SPINNING_WHEEL, SpinningWheelRecipe.class));

    public static final IRecipeType<PressRecipe> PRESS = IRecipeType.register(MOD_ID + "press");
    public static final RegistryObject<GenericSerializer<PressRecipe>> PRESS_SERIALIZER = RECIPE_SERIALIZER.register("press",
            () -> new GenericSerializer<>(PRESS, PressRecipe.class));

    public static final IRecipeType<AlembicRecipe> ALEMBIC = IRecipeType.register(MOD_ID + "alembic");
    public static final RegistryObject<GenericSerializer<AlembicRecipe>> ALEMBIC_SERIALIZER = RECIPE_SERIALIZER.register("alembic",
            () -> new GenericSerializer<>(ALEMBIC, AlembicRecipe.class));

    public static final IRecipeType<SeasoningBarrelRecipe> SEASONING_BARREL = IRecipeType.register(MOD_ID + "seasoning_barrel");
    public static final RegistryObject<GenericSerializer<SeasoningBarrelRecipe>> SEASONING_BARREL_SERIALIZER = RECIPE_SERIALIZER.register("seasoning_barrel",
            () -> new GenericSerializer<>(SEASONING_BARREL, SeasoningBarrelRecipe.class));

    public static final IRecipeType<SinteringFurnaceRecipe> SINTERING_FURNACE = IRecipeType.register(MOD_ID + "sintering_furnace");
    public static final RegistryObject<GenericSerializer<SinteringFurnaceRecipe>> SINTERING_FURNACE_SERIALIZER = RECIPE_SERIALIZER.register("sintering_furnace",
            () -> new GenericSerializer<>(SINTERING_FURNACE, SinteringFurnaceRecipe.class));

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZER.register(bus);
    }
}