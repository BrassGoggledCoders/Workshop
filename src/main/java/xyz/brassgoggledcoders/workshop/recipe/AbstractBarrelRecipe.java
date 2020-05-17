package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public abstract class AbstractBarrelRecipe extends WorkshopRecipe {
    public Ingredient itemIn = Ingredient.EMPTY;
    public FluidStack fluidIn = FluidStack.EMPTY;
    public ItemStack itemOut = ItemStack.EMPTY;
    public FluidStack fluidOut = FluidStack.EMPTY;
    public int seasoningTime = 1000;

    public AbstractBarrelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public AbstractBarrelRecipe(ResourceLocation resourceLocation, Ingredient itemIn, ItemStack itemOut, FluidStack fluidIn, FluidStack fluidOut, int seasoningTime) {
        this(resourceLocation);
        this.itemIn = itemIn;
        this.itemOut = itemOut;
        this.fluidIn = fluidIn;
        this.fluidOut = fluidOut;
        this.seasoningTime = seasoningTime;
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
        return seasoningTime;
    }
}
