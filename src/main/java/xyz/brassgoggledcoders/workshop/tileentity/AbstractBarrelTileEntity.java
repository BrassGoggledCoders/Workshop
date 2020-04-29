package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.recipe.AbstractBarrelRecipe;

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
                "inputInventory", 29, 42, 1, pos++)
                .setColor(DyeColor.LIGHT_BLUE)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.inputFluidTank = new SidedFluidTankComponent<T>(
                "inputFluidTank", tankSize, 52, 20, pos++)
                .setColor(DyeColor.BROWN)
                .setTankAction(SidedFluidTankComponent.Action.FILL)
                .setOnContentChange(this.getMachineComponent()::forceRecipeRecheck));
        this.getMachineComponent().addInventory(this.outputInventory = new SidedInventoryComponent<T>(
                "outputInventory", 130, 42, 1, pos++)
                .setColor(DyeColor.BLUE)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().addTank(this.outputFluidTank = new SidedFluidTankComponent<T>(
                "outputFluidTank", tankSize, 105, 20, pos++)
                .setColor(DyeColor.BLACK)
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
    public CompoundNBT write(CompoundNBT compound) {
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
        //for (int i = 0; i < outputInventory.getSlots(); i++) {
        ItemStack itemOut = currentRecipe.itemOut;
        if (itemOut != null) {
            outputInventory.insertItem(0, itemOut, false);
        }
        //}
    }

    @Override
    public boolean matchesInputs(R currentRecipe) {
        return currentRecipe.matches(inputInventory, inputFluidTank);
    }

    @Override
    public int getProcessingTime(R currentRecipe) {
        return currentRecipe.getProcessingTime();
    }
}