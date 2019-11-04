package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class PressRecipes extends SerializableRecipe {

    public static GenericSerializer<PressRecipes> SERIALIZER = new GenericSerializer<>(new ResourceLocation(MOD_ID, "press"), PressRecipes.class);
    public static List<PressRecipes> RECIPES = new ArrayList<>();

    public ItemStack input;
    public ItemStack output;

    public PressRecipes(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public PressRecipes(ResourceLocation resourceLocation, ItemStack input, ItemStack output) {
        super(resourceLocation);
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
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
