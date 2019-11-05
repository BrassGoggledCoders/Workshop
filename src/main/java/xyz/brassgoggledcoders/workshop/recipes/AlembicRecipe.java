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
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class AlembicRecipe extends SerializableRecipe {

    public static GenericSerializer<AlembicRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(MOD_ID, "alembic"), AlembicRecipe.class);
    public static List<AlembicRecipe> RECIPES = new ArrayList<>();

    public Ingredient.IItemList input;
    public ItemStack container;
    public ItemStack output;
    public ItemStack[] residue;
    public int cooldownTime;

    public AlembicRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public AlembicRecipe(ResourceLocation resourceLocation, Ingredient.IItemList input, ItemStack output, ItemStack container, ItemStack[] residue, int cooldownTime) {
        super(resourceLocation);
        this.input = input;
        this.output = output;
        this.container = container;
        this.residue = residue;
        this.cooldownTime = cooldownTime;
        RECIPES.add(this);
    }

    public boolean matches(IItemHandler itemIn, IItemHandler containerIn) {
        List<ItemStack> handlerItems = new ArrayList<>();
        for (int i = 0; i < itemIn.getSlots(); i++) {
            if (!itemIn.getStackInSlot(i).isEmpty()) handlerItems.add(itemIn.getStackInSlot(i).copy());
        }
        for (ItemStack stack : input.getStacks()) {
            boolean found = false;
            int i = 0;
            for (; i < handlerItems.size(); i++) {
                if (handlerItems.get(i).isItemEqual(stack)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                handlerItems.remove(i);
                break;
            }

            if (!found) return false;
        }
        return handlerItems.size() == 0 && this.container.isItemEqual(containerIn.getStackInSlot(0));
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
