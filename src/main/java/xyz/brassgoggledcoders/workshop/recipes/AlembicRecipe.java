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

import static xyz.brassgoggledcoders.workshop.WorkShop.MOD_ID;

public class AlembicRecipe extends SerializableRecipe {

    public static GenericSerializer<AlembicRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(MOD_ID, "alembic"), AlembicRecipe.class);
    public static List<AlembicRecipe> RECIPES = new ArrayList<>();

    static {

    }

    public Ingredient.IItemList input;
    public ItemStack container;
    public ItemStack output;
    public ItemStack[] residue;
    public boolean defaultRecipe;

    public AlembicRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public AlembicRecipe(ResourceLocation resourceLocation, Ingredient.IItemList input, ItemStack output, ItemStack container, ItemStack[] residue, boolean defaultRecipe) {
        super(resourceLocation);

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
        return input.getStacks().iterator().next();
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
