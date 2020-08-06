package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;

import javax.annotation.Nonnull;

public class DryingBasinRecipe extends WorkshopRecipe {
    public Ingredient itemIn = Ingredient.EMPTY;
    public FluidStack fluidIn = FluidStack.EMPTY;
    public ItemStack itemOut = ItemStack.EMPTY;
    public int dryingTime = 1000;

    public DryingBasinRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return WorkshopRecipes.DRYING_BASIN_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return WorkshopRecipes.DRYING_BASIN_SERIALIZER.get().getRecipeType();
    }

    public DryingBasinRecipe(ResourceLocation resourceLocation, Ingredient itemIn, ItemStack itemOut, FluidStack fluidIn, int seasoningTime) {
        this(resourceLocation);
        this.itemIn = itemIn;
        this.itemOut = itemOut;
        this.fluidIn = fluidIn;
        this.dryingTime = seasoningTime;
    }

    public boolean matches(@Nonnull IItemHandler handler, @Nonnull FluidTankComponent<?> tank) {
        return itemIn.test(handler.getStackInSlot(0)) &&
                tank.drainForced(fluidIn, IFluidHandler.FluidAction.SIMULATE).getAmount() == fluidIn.getAmount();
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull IInventory inv) {
        return itemOut;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return itemOut;
    }

    @Override
    public int getProcessingTime() {
        return dryingTime;
    }
}
