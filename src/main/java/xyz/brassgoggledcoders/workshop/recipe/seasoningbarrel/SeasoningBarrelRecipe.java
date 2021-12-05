package xyz.brassgoggledcoders.workshop.recipe.seasoningbarrel;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class SeasoningBarrelRecipe implements IRecipe<SeasoningBarrelRecipeInventory> {
    private final ResourceLocation id;
    private final Ingredient itemInput;
    private final ITag<Fluid> fluidInput;
    private final ItemStack itemOutput;
    private final FluidStack fluidOutput;

    public SeasoningBarrelRecipe(ResourceLocation id, Ingredient itemInput, ITag<Fluid> fluidInput, ItemStack itemOutput, FluidStack fluidOutput) {
        this.id = id;
        this.itemInput = itemInput;
        this.fluidInput = fluidInput;
        this.itemOutput = itemOutput;
        this.fluidOutput = fluidOutput;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(SeasoningBarrelRecipeInventory pInv, World pLevel) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull SeasoningBarrelRecipeInventory pInv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem() {
        return null;
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return null;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return null;
    }
}
