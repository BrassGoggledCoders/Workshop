package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class SpinningWheelRecipe extends SerializableRecipe {

    public static GenericSerializer<SpinningWheelRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(MOD_ID, "spinning_wheel"), SpinningWheelRecipe.class);
    public static List<SpinningWheelRecipe> RECIPES = new ArrayList<>();

    public Ingredient.IItemList[] itemsIn;
    public ItemStack itemOut;

    public SpinningWheelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SpinningWheelRecipe(ResourceLocation resourceLocation, ItemStack itemOut, Ingredient.IItemList[] itemsIn) {
        super(resourceLocation);
        this.itemsIn = itemsIn;
        this.itemOut = itemOut;
        RECIPES.add(this);
    }

    public boolean matches(IItemHandler inv) {
        List<ItemStack> handlerItems = new ArrayList<>();
        for (int i = 0; i < inv.getSlots(); i++) {
            if (!inv.getStackInSlot(i).isEmpty()) handlerItems.add(inv.getStackInSlot(i).copy());
        }
        for (Ingredient.IItemList iItemList : itemsIn) {
            boolean found = false;
            for (ItemStack stack : iItemList.getStacks()) {
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
            }
            if (!found) return false;
        }
        return handlerItems.size() == 0;
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
        return itemOut;
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
