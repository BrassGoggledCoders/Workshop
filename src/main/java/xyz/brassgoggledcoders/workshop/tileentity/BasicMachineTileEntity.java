package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.component.machine.IRecipeMachineHarness;
import xyz.brassgoggledcoders.workshop.component.machine.MachineComponent;
import xyz.brassgoggledcoders.workshop.component.machine.RecipeMachineComponent;
import xyz.brassgoggledcoders.workshop.recipe.IMachineRecipe;

import javax.annotation.Nonnull;

public abstract class BasicMachineTileEntity<T extends BasicMachineTileEntity<T, U>, U extends IRecipe<IInventory> & IMachineRecipe>
        extends BasicInventoryTileEntity<T> implements IRecipeMachineHarness<T, U> {

    public BasicMachineTileEntity(TileEntityType<T> tileEntityType, ProgressBarComponent<T> progressBar) {
        super(tileEntityType);
        this.createMachineComponent(new RecipeMachineComponent<>(this.getSelf(), this::getPos, progressBar));
    }

    //TODO
    @Override
    public RecipeMachineComponent<T, U> getMachineComponent() {
        return (RecipeMachineComponent) super.getMachineComponent();
    }

    @Override
    public int getProcessingTime(U currentRecipe) {
        return currentRecipe.getProcessingTime();
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        this.getMachineComponent().getPrimaryBar().deserializeNBT(compound.getCompound("progress"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("progress", this.getMachineComponent().getPrimaryBar().serializeNBT());
        return super.write(compound);
    }

    public abstract ResourceLocation getRecipeCategoryUID();
}
