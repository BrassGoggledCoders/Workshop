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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.SEASONING_BARREL_SERIALIZER;

public class SeasoningBarrelRecipe extends SerializableRecipe {
    public Ingredient itemIn = Ingredient.EMPTY;
    public ItemStack itemOut = ItemStack.EMPTY;
    public FluidStack fluidInput = FluidStack.EMPTY;
    public FluidStack fluidOut = FluidStack.EMPTY;
    public int seasoningTime = 1000;

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public boolean matches(@Nonnull IItemHandler handler,@Nonnull IFluidHandler tank) {
        return itemIn.test(handler.getStackInSlot(0)) &&
                tank.drain(fluidInput, IFluidHandler.FluidAction.SIMULATE).getAmount() == fluidInput.getAmount();
    }

    @Override
    public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return itemOut;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SEASONING_BARREL_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return SEASONING_BARREL_SERIALIZER.get().getRecipeType();
    }
}
