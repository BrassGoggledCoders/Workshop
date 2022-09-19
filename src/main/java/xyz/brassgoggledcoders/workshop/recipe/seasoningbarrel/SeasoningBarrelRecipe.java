package xyz.brassgoggledcoders.workshop.recipe.seasoningbarrel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.FluidAndItemRecipeContainer;
import xyz.brassgoggledcoders.workshop.recipe.IFluidOutputRecipe;
import xyz.brassgoggledcoders.workshop.recipe.ingredient.fluid.FluidIngredient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SeasoningBarrelRecipe implements Recipe<FluidAndItemRecipeContainer>, IFluidOutputRecipe<FluidAndItemRecipeContainer> {
    private final ResourceLocation id;
    private final Ingredient itemInput;
    private final FluidIngredient fluidInput;
    private final ItemStack itemOutput;
    private final FluidStack fluidOutput;

    public SeasoningBarrelRecipe(ResourceLocation id, Ingredient itemInput, FluidIngredient fluidInput,
                                 ItemStack itemOutput, FluidStack fluidOutput) {
        this.id = id;
        this.itemInput = itemInput;
        this.fluidInput = fluidInput;
        this.itemOutput = itemOutput;
        this.fluidOutput = fluidOutput;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(FluidAndItemRecipeContainer container, Level pLevel) {
        return itemInput.test(container.getItem(0)) && fluidInput.test(container.getFluidInTank(0));
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull FluidAndItemRecipeContainer pInv) {
        return itemOutput.copy();
    }

    @Override
    @Nonnull
    public FluidStack assembleFluid(@Nonnull FluidAndItemRecipeContainer pInv) {
        return fluidOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return itemOutput;
    }

    @Override
    @Nonnull
    public FluidStack getResultFluid() {
        return fluidOutput;
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return WorkshopRecipes.SEASONING_BARREL_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return WorkshopRecipes.SEASONING_BARREL_TYPE.get();
    }
}
