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
import static xyz.brassgoggledcoders.workshop.registries.Recipes.SEASONING_BARREL_SERIALIZER;
import static xyz.brassgoggledcoders.workshop.registries.Recipes.SINTERING_FURNACE_SERIALIZER;

public class SinteringFurnaceRecipe extends SerializableRecipe {

    public Ingredient.IItemList[] powderIn;
    public ItemStack itemIn;
    public ItemStack output;
    public int meltTime;

    public SinteringFurnaceRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    public boolean matches(IItemHandler powderInv, IItemHandler itemInv) {
        List<ItemStack> handlerItems = new ArrayList<>();
        for (int i = 0; i < powderInv.getSlots(); i++) {
            if (!powderInv.getStackInSlot(i).isEmpty()) handlerItems.add(powderInv.getStackInSlot(i).copy());
        }
        for (Ingredient.IItemList iItemList : powderIn) {
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
        return handlerItems.size() == 0 && this.itemIn.isItemEqual(itemInv.getStackInSlot(0));
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
        return SINTERING_FURNACE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return SINTERING_FURNACE_SERIALIZER.get().getRecipeType();
    }
}
