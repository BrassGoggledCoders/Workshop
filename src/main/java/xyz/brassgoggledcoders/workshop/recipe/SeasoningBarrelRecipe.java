package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
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

public class SeasoningBarrelRecipe extends SerializableRecipe implements IMachineRecipe {
    public Ingredient itemIn = Ingredient.EMPTY;
    public ItemStack itemOut = ItemStack.EMPTY;
    public FluidStack fluidIn = FluidStack.EMPTY;
    public FluidStack fluidOut = FluidStack.EMPTY;
    public int seasoningTime = 1000;

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation, Ingredient itemIn, ItemStack itemOut, FluidStack fluidIn, FluidStack fluidOut, int seasoningTime) {
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
    @Nonnull
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SEASONING_BARREL_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return SEASONING_BARREL_SERIALIZER.get().getRecipeType();
    }

    @Override
    public int getProcessingTime() {
        return seasoningTime;
    }
}
