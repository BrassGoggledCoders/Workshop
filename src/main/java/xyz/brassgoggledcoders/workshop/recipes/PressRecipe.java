package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

import static xyz.brassgoggledcoders.workshop.registries.Recipes.PRESS_SERIALIZER;


public class PressRecipe extends SerializableRecipe {

    public ItemStack itemIn = ItemStack.EMPTY;
    public FluidStack fluidOut = FluidStack.EMPTY;

    public PressRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public boolean matches(IItemHandler inv) {
        return this.itemIn.isItemEqual(inv.getStackInSlot(0));
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
        return ItemStack.EMPTY;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return PRESS_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return PRESS_SERIALIZER.get().getRecipeType();
    }

}
