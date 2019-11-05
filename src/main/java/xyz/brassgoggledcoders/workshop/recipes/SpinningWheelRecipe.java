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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class SpinningWheelRecipe extends SerializableRecipe {

    public static GenericSerializer<SpinningWheelRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(MOD_ID, "spinningwheel"), SpinningWheelRecipe.class);
    public static List<SpinningWheelRecipe> RECIPES = new ArrayList<>();

    public Ingredient.IItemList input;
    public ItemStack output;

    public SpinningWheelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SpinningWheelRecipe(ResourceLocation resourceLocation, Ingredient.IItemList input, ItemStack output) {
        super(resourceLocation);
        this.input = input;
        this.output = output;
        RECIPES.add(this);
    }

    public boolean matches(World world, BlockPos pos) {
        return Ingredient.fromItemListStream(Arrays.asList(this.input).stream()).test(new ItemStack(world.getBlockState(pos).getBlock()));
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
