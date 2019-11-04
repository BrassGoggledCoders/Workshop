package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class SeasoningBarrelRecipe extends SerializableRecipe {

    public static GenericSerializer<SeasoningBarrelRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(MOD_ID, "seasoningbarrel"), SeasoningBarrelRecipe.class);
    public static List<SeasoningBarrelRecipe> RECIPES = new ArrayList<>();

    public ItemStack product;
    public ItemStack catalyst;
    public FluidStack input;
    public FluidStack output;
    public int seasoningTime;

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation, FluidStack input, ItemStack catalyst, FluidStack output, ItemStack product, int seasoningTime) {
        super(resourceLocation);
        this.input = input;
        this.output = output;
        this.catalyst = catalyst;
        this.product = product;
        this.seasoningTime = seasoningTime;
        RECIPES.add(this);
    }


    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return product;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }
}
