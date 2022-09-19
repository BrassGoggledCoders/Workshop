package xyz.brassgoggledcoders.workshop.content;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.recipe.seasoningbarrel.SeasoningBarrelRecipe;
import xyz.brassgoggledcoders.workshop.recipe.seasoningbarrel.SeasoningBarrelRecipeSerializer;

public class WorkshopRecipes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(
            Registry.RECIPE_TYPE_REGISTRY,
            Workshop.ID
    );

    public static final RegistryObject<RecipeType<SeasoningBarrelRecipe>> SEASONING_BARREL_TYPE = createType("seasoning_barrel");

    public static final RegistryEntry<RecipeSerializer<SeasoningBarrelRecipe>> SEASONING_BARREL_SERIALIZER = Workshop.getRegistrate()
            .object("seasoning_barrel")
            .simple(Registry.RECIPE_SERIALIZER_REGISTRY, SeasoningBarrelRecipeSerializer::new);

    public static void setup() {

    }

    public static <T extends Recipe<U>, U extends Container> RegistryObject<RecipeType<T>> createType(String name) {
        return RECIPE_TYPE_DEFERRED_REGISTER.register(
                name,
                () -> new RecipeType<>() {
                    @Override
                    public String toString() {
                        return Workshop.ID + ":" + name;
                    }
                }
        );
    }
}
