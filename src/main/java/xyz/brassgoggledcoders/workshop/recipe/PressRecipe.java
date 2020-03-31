package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.PRESS_SERIALIZER;


public class PressRecipe extends SerializableRecipe {

    public Ingredient itemIn = Ingredient.EMPTY;
    public FluidStack fluidOut = FluidStack.EMPTY;

    public PressRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public PressRecipe(ResourceLocation resourceLocation, Ingredient itemIn, FluidStack fluidOut) {
        this(resourceLocation);
        this.itemIn = itemIn;
        this.fluidOut = fluidOut;
    }

    public boolean matches(IItemHandler handler) {
        return itemIn.test(handler.getStackInSlot(0));
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
