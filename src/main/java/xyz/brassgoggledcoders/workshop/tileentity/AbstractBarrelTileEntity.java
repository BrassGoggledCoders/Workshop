package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.recipe.AbstractBarrelRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public abstract class AbstractBarrelTileEntity<T extends BasicMachineTileEntity<T, R>, R extends AbstractBarrelRecipe> extends BasicMachineTileEntity<T, R> {

    protected static final int tankSize = 4000; // mB

    protected final InventoryComponent<T> inputInventory;
    protected final FluidTankComponent<T> inputFluidTank;
    protected final InventoryComponent<T> outputInventory;
    protected final FluidTankComponent<T> outputFluidTank;

    public AbstractBarrelTileEntity(TileEntityType<T> tileEntityType, ProgressBarComponent<T> progressBar) {
        super(tileEntityType, progressBar);
        int pos = 0;
        this.getMachineComponent().addInventory(this.inputInventory = new SidedInventoryComponent<T>(
                InventoryUtil.ITEM_INPUT, 29, 42, 1, pos++)
                .setColor(InventoryUtil.ITEM_INPUT_COLOR)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.inputFluidTank = new SidedFluidTankComponent<T>(
                InventoryUtil.FLUID_INPUT, tankSize, 52, 20, pos++)
                .setColor(InventoryUtil.FLUID_INPUT_COLOR)
                .setTankAction(SidedFluidTankComponent.Action.FILL)
                .setOnContentChange(this.getMachineComponent()::forceRecipeRecheck));
        this.getMachineComponent().addInventory(this.outputInventory = new SidedInventoryComponent<T>(
                InventoryUtil.ITEM_OUTPUT, 130, 42, 1, pos++)
                .setColor(InventoryUtil.ITEM_OUTPUT_COLOR)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().addTank(this.outputFluidTank = new SidedFluidTankComponent<T>(
                InventoryUtil.FLUID_OUTPUT, tankSize, 105, 20, pos++)
                .setColor(InventoryUtil.FLUID_OUTPUT_COLOR)
                .setTankAction(SidedFluidTankComponent.Action.DRAIN));
    }

    @Override
    public void read(CompoundNBT compound) {
        inputInventory.deserializeNBT(compound.getCompound("inputInventory"));
        inputFluidTank.readFromNBT(compound.getCompound("inputFluidTank"));
        outputInventory.deserializeNBT(compound.getCompound("outputInventory"));
        outputFluidTank.readFromNBT(compound.getCompound("outputFluidTank"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("inputInventory", inputInventory.serializeNBT());
        compound.put("inputFluidTank", inputFluidTank.writeToNBT(new CompoundNBT()));
        compound.put("outputInventory", outputInventory.serializeNBT());
        compound.put("outputFluidTank", outputFluidTank.writeToNBT(new CompoundNBT()));
        return super.write(compound);
    }

    @Override
    public boolean hasInputs() {
        return !inputInventory.getStackInSlot(0).isEmpty() || !inputFluidTank.isEmpty();
    }

    @Override
    public void handleComplete(R currentRecipe) {
        inputFluidTank.drainForced(currentRecipe.fluidIn, IFluidHandler.FluidAction.EXECUTE);
        for (int i = 0; i < inputInventory.getSlots(); i++) {
            inputInventory.getStackInSlot(i).shrink(1);
        }
        if (outputFluidTank.getFluid().equals(currentRecipe.fluidOut) || outputFluidTank.isEmpty()) {
            int capacity = outputFluidTank.getCapacity();
            if (capacity >= outputFluidTank.getFluid().getAmount()
                    + currentRecipe.fluidOut.getAmount()) {
                outputFluidTank.fillForced(currentRecipe.fluidOut.copy(),
                        IFluidHandler.FluidAction.EXECUTE);
            }
        }
        if(!currentRecipe.itemOut.isEmpty()) {
            ItemHandlerHelper.insertItem(outputInventory, currentRecipe.itemOut.copy(), false);
        }
    }

    @Override
    public boolean matchesInputs(R currentRecipe) {
        return ItemHandlerHelper.insertItemStacked(this.outputInventory, currentRecipe.itemOut, true).isEmpty() &&
                this.outputFluidTank.fill(currentRecipe.fluidOut, IFluidHandler.FluidAction.SIMULATE) == 0 &&
                currentRecipe.matches(inputInventory, inputFluidTank);
    }

    @Override
    public int getProcessingTime(R currentRecipe) {
        return currentRecipe.getProcessingTime();
    }
}
