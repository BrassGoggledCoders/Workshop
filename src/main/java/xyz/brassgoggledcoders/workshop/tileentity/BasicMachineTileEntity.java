package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.component.machine.IRecipeMachineHarness;
import xyz.brassgoggledcoders.workshop.component.machine.RecipeMachineComponent;
import xyz.brassgoggledcoders.workshop.recipe.IMachineRecipe;

public abstract class BasicMachineTileEntity<T extends BasicMachineTileEntity<T, U>, U extends IRecipe<IInventory> & IMachineRecipe>
        extends BasicInventoryTileEntity<T> implements IRecipeMachineHarness<T, U> {

    public BasicMachineTileEntity(TileEntityType<T> tileEntityType, ProgressBarComponent<T> progressBar) {
        super(tileEntityType);
        this.createMachineComponent(new RecipeMachineComponent<>(this.getSelf(), this::getPos, progressBar));
    }

    @Override
    public RecipeMachineComponent<T, U> getMachineComponent() {
        return (RecipeMachineComponent<T, U>) super.getMachineComponent();
    }

    @Override
    public int getProcessingTime(U currentRecipe) {
        return currentRecipe.getProcessingTime();
    }

    public abstract ResourceLocation getRecipeCategoryUID();
}
