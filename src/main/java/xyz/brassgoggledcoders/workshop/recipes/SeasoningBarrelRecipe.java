package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import static xyz.brassgoggledcoders.workshop.registries.Recipes.SEASONING_BARREL_SERIALIZER;

public class SeasoningBarrelRecipe extends SerializableRecipe {
    public ItemStack itemIn = ItemStack.EMPTY;
    public ItemStack itemOut = ItemStack.EMPTY;
    public FluidStack fluidInput = FluidStack.EMPTY;
    public FluidStack fluidOut = FluidStack.EMPTY;
    public int seasoningTime = 1000;

    public SeasoningBarrelRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public boolean matches(IItemHandler handler, PosFluidTank tank) {
        if (itemOut == null || tank == null || fluidInput == null) return false;
        return handler.getStackInSlot(0).isItemEqual(itemIn) && tank.drainForced(fluidInput, IFluidHandler.FluidAction.SIMULATE).getAmount() == fluidInput.getAmount();
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
        return SEASONING_BARREL_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return SEASONING_BARREL_SERIALIZER.get().getRecipeType();
    }
}
