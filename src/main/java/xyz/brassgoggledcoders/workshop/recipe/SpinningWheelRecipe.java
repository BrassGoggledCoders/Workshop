package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.SPINNING_WHEEL_SERIALIZER;

public class SpinningWheelRecipe extends WorkshopRecipe {

    public Ingredient[] inputs = new Ingredient[0];
    public ItemStack output = ItemStack.EMPTY;
    public int processingTime;

    public SpinningWheelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SPINNING_WHEEL_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return SPINNING_WHEEL_SERIALIZER.get().getRecipeType();
    }

    public SpinningWheelRecipe(ResourceLocation name, Ingredient[] input, ItemStack output, int processingTime) {
        this(name);
        this.inputs = input;
        this.output = output;
        this.processingTime = processingTime;
    }

    public boolean matches(IItemHandler inv) {
        return InventoryUtil.inventoryHasIngredients(inv, inputs);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public int getProcessingTime() {
        return processingTime;
    }
}
