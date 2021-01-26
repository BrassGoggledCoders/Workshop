package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.component.machine.FixedSidedInventoryComponent;
import xyz.brassgoggledcoders.workshop.component.machine.FixedSidedTankComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.DryingBasinRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class DryingBasinTileEntity extends BasicMachineTileEntity<DryingBasinTileEntity, DryingBasinRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.DRYING_BASIN_SERIALIZER.get().getRecipeType().toString());
    public static final int tankSize = 4000; // mB

    protected final InventoryComponent<DryingBasinTileEntity> inputInventory;
    protected final FluidTankComponent<DryingBasinTileEntity> inputFluidTank;
    protected final InventoryComponent<DryingBasinTileEntity> outputInventory;

    public DryingBasinTileEntity() {
        super(WorkshopBlocks.DRYING_BASIN.getTileEntityType(), new ProgressBarComponent<DryingBasinTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.ARROW_RIGHT));
        this.getMachineComponent().addInventory(this.inputInventory = new FixedSidedInventoryComponent<DryingBasinTileEntity>(
                InventoryUtil.ITEM_INPUT, 29, 42, 1, FixedSidedInventoryComponent.NOT_BOTTOM)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.inputFluidTank = new FixedSidedTankComponent<DryingBasinTileEntity>(
                InventoryUtil.FLUID_INPUT, tankSize, 52, 20, FixedSidedInventoryComponent.NOT_BOTTOM)
                .setTankAction(SidedFluidTankComponent.Action.FILL)
                .setOnContentChange(this.getMachineComponent()::forceRecipeRecheck));
        this.getMachineComponent().addInventory(this.outputInventory = new FixedSidedInventoryComponent<DryingBasinTileEntity>(
                InventoryUtil.ITEM_OUTPUT, 130, 42, 1, FacingUtil.Sideness.BOTTOM)
                .setInputFilter((stack, integer) -> false));
    }

    @Override
    public DryingBasinTileEntity getSelf() {
        return this;
    }

    public InventoryComponent<DryingBasinTileEntity> getInputInventory() {
        return inputInventory;
    }

    public FluidTankComponent<DryingBasinTileEntity> getInputFluidTank() {
        return inputFluidTank;
    }

    public InventoryComponent<DryingBasinTileEntity> getOutputInventory() {
        return outputInventory;
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        inputInventory.deserializeNBT(compound.getCompound("inputInventory"));
        inputFluidTank.readFromNBT(compound.getCompound("inputFluidTank"));
        outputInventory.deserializeNBT(compound.getCompound("outputInventory"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("inputInventory", inputInventory.serializeNBT());
        compound.put("inputFluidTank", inputFluidTank.writeToNBT(new CompoundNBT()));
        compound.put("outputInventory", outputInventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public ResourceLocation getRecipeCategoryUID() {
        return ID;
    }

    @Override
    public boolean hasInputs() {
        return !inputInventory.getStackInSlot(0).isEmpty() || !inputFluidTank.isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.DRYING_BASIN_SERIALIZER.get().getRecipeType() && recipe instanceof DryingBasinRecipe;
    }

    @Override
    public DryingBasinRecipe castRecipe(IRecipe<?> iRecipe) {
        return (DryingBasinRecipe) iRecipe;
    }

    @Override
    public void handleComplete(DryingBasinRecipe currentRecipe) {
        inputFluidTank.drainForced(currentRecipe.fluidIn, IFluidHandler.FluidAction.EXECUTE);
        for (int i = 0; i < inputInventory.getSlots(); i++) {
            inputInventory.getStackInSlot(i).shrink(1);
        }
        if (!currentRecipe.itemOut.isEmpty()) {
            ItemHandlerHelper.insertItem(outputInventory, currentRecipe.itemOut.copy(), false);
        }
    }

    @Override
    public boolean matchesInputs(DryingBasinRecipe currentRecipe) {
        return ItemHandlerHelper.insertItemStacked(this.outputInventory, currentRecipe.itemOut, true).isEmpty() &&
                currentRecipe.matches(inputInventory, inputFluidTank);
    }

    @Override
    public int getProcessingTime(DryingBasinRecipe currentRecipe) {
        return currentRecipe.getProcessingTime();
    }
}
