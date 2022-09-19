package xyz.brassgoggledcoders.workshop.recipe;

import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public interface IFluidOutputRecipe<T> {
    @NotNull
    FluidStack assembleFluid(T container);

    @NotNull
    FluidStack getResultFluid();
}
