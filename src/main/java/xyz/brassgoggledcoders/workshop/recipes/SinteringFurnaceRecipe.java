package xyz.brassgoggledcoders.workshop.recipes;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.SINTERING_FURNACE_SERIALIZER;

import java.util.ArrayList;
import java.util.List;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class SinteringFurnaceRecipe extends SerializableRecipe {

    public Ingredient.IItemList[] powderIn;
    public ItemStack itemIn = ItemStack.EMPTY;
    public ItemStack itemOut = ItemStack.EMPTY;
    public int meltTime = 240;

    public SinteringFurnaceRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public boolean matches(IItemHandler powder, IItemHandler itemInv) {
        List<ItemStack> handlerItems = new ArrayList<>();
        for (int i = 0; i < powder.getSlots(); i++) {
            if (!powder.getStackInSlot(i).isEmpty()) handlerItems.add(powder.getStackInSlot(i).copy());
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
        return handlerItems.size() == 0 && itemInv.getStackInSlot(0).isItemEqual(itemIn);
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
        return SINTERING_FURNACE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return SINTERING_FURNACE_SERIALIZER.get().getRecipeType();
    }

}
