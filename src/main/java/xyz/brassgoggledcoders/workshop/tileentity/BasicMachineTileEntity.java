package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import xyz.brassgoggledcoders.workshop.component.machine.IRecipeMachineHarness;
import xyz.brassgoggledcoders.workshop.component.machine.RecipeMachineComponent;
import xyz.brassgoggledcoders.workshop.recipe.IMachineRecipe;

import javax.annotation.Nonnull;

public abstract class BasicMachineTileEntity<T extends BasicMachineTileEntity<T, U>, U extends IRecipe<IInventory> & IMachineRecipe>
        extends BasicInventoryTileEntity<T> implements IRecipeMachineHarness<T, U> {
    private final RecipeMachineComponent<T, U> machineComponent;

    public BasicMachineTileEntity(TileEntityType<T> tileEntityType, ProgressBarComponent<T> progressBar) {
        super(tileEntityType);
        this.machineComponent = new RecipeMachineComponent<>(this.getSelf(), this::getPos, progressBar);
    }

    @Override
    public RecipeMachineComponent<T, U> getMachineComponent() {
        return this.machineComponent;
    }

    @Override
    public int getProcessingTime(U currentRecipe) {
        return currentRecipe.getProcessingTime();
    }

    @Override
    public void read(CompoundNBT compound) {
        this.getMachineComponent().getPrimaryBar().deserializeNBT(compound.getCompound("progress"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("progress", this.getMachineComponent().getPrimaryBar().serializeNBT());
        return super.write(compound);
    }
}
