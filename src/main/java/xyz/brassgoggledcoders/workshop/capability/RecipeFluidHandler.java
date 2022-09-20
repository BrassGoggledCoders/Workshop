package xyz.brassgoggledcoders.workshop.capability;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class RecipeFluidHandler implements IFluidHandler {
    private final int capacity;

    private FluidStack inputStack;
    private FluidStack outputStack;

    public RecipeFluidHandler(int capacity) {
        this.capacity = capacity;
        this.inputStack = FluidStack.EMPTY;
        this.outputStack = FluidStack.EMPTY;
    }

    @Override
    public int getTanks() {
        return 2;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return tank == 0 ? inputStack : outputStack;
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.capacity - (tank == 0 ? this.outputStack : this.inputStack).getAmount();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}
