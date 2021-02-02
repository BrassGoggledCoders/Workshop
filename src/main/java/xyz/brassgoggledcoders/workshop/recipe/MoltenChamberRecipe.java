package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.util.FluidTagInput;

import javax.annotation.Nonnull;

public class MoltenChamberRecipe extends AbstractBarrelRecipe {

    public MoltenChamberRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public MoltenChamberRecipe(ResourceLocation resourceLocation, Ingredient itemIn, ItemStack itemOut, FluidTagInput fluidIn, FluidStack fluidOut, int seasoningTime) {
        super(resourceLocation, itemIn, itemOut, fluidIn, fluidOut, seasoningTime);
    }

    @Override
    @Nonnull
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return WorkshopRecipes.MOLTEN_CHAMBER_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return WorkshopRecipes.MOLTEN_CHAMBER_SERIALIZER.get().getRecipeType();
    }
}