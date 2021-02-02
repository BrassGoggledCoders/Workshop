package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.util.FluidTagInput;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.SEASONING_BARREL_SERIALIZER;

public class SeasoningBarrelRecipe extends AbstractBarrelRecipe {

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation, Ingredient itemIn, ItemStack itemOut, FluidTagInput fluidIn, FluidStack fluidOut, int seasoningTime) {
        super(resourceLocation, itemIn, itemOut, fluidIn, fluidOut, seasoningTime);
    }

    @Override
    @Nonnull
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SEASONING_BARREL_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return SEASONING_BARREL_SERIALIZER.get().getRecipeType();
    }
}
