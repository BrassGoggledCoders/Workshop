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
import xyz.brassgoggledcoders.workshop.tileentity.MortarTileEntity;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.MORTAR_SERIALIZER;

public class MortarRecipe extends WorkshopRecipe {

    public Ingredient[] input = new Ingredient[0];
    public FluidStack fluidInput = FluidStack.EMPTY;
    public ItemStack output = ItemStack.EMPTY;
    public int processingTime = 10;

    public MortarRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public MortarRecipe(ResourceLocation name, Ingredient[] input, FluidStack fluidInput, ItemStack output, int cooldownTime) {
        this(name);
        this.input = input;
        this.fluidInput = fluidInput;
        this.output = output;
        this.processingTime = cooldownTime;
    }

    public boolean matches(@Nonnull IItemHandler handler, FluidTankComponent<MortarTileEntity> tank) {
        //For each ingredient in the input list check if any of the slots in the handler match the Ingredient predicate
        return InventoryUtil.inventoryHasIngredients(handler, input) && tank.drainForced(fluidInput, IFluidHandler.FluidAction.SIMULATE).getAmount() == fluidInput.getAmount();
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
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return MORTAR_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return MORTAR_SERIALIZER.get().getRecipeType();
    }

    @Override
    public int getProcessingTime() {
        return this.processingTime;
    }
}
