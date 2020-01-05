package xyz.brassgoggledcoders.workshop.registries;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.recipes.SpinningWheelRecipe;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class Recipes {

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final IRecipeType<SpinningWheelRecipe> SPINNING_WHEEL = IRecipeType.register(MOD_ID + "spinning_wheel");
    public static final RegistryObject<GenericSerializer<SpinningWheelRecipe>> SPINNING_WHEEL_SERIALIZER = RECIPE_SERIALIZER.register("spinning_wheel",
            () -> new GenericSerializer<>(SPINNING_WHEEL, SpinningWheelRecipe.class));

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZER.register(bus);
    }
}
