package xyz.brassgoggledcoders.workshop.component.machine;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

public interface IRecipeMachineHarness<T extends IRecipeMachineHarness<T, U>, U extends IRecipe<IInventory>> extends IMachineHarness<T> {
    boolean hasInputs();

    boolean checkRecipe(IRecipe<?> recipe);

    U castRecipe(IRecipe<?> iRecipe);

    int getProcessingTime(U currentRecipe);

    boolean matchesInputs(U currentRecipe);

    void handleComplete(U currentRecipe);
}
