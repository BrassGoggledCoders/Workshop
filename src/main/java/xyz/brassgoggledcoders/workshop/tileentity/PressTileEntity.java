package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class PressTileEntity extends BasicMachineTileEntity<PressTileEntity, PressRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.PRESS_SERIALIZER.get().getRecipeType().toString());

    private final InventoryComponent<PressTileEntity> inputInventory;
    private final FluidTankComponent<PressTileEntity> outputFluid;

    private boolean triggered = false;

    public PressTileEntity() {
        super(WorkshopBlocks.PRESS.getTileEntityType(), new ProgressBarComponent<PressTileEntity>(70, 40, 120).
                setBarDirection(ProgressBarComponent.BarDirection.ARROW_RIGHT));
        this.getMachineComponent().addInventory(this.inputInventory = new InventoryComponent<PressTileEntity>(InventoryUtil.ITEM_INPUT, 45, 50, 1)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.outputFluid = new FluidTankComponent<PressTileEntity>(InventoryUtil.FLUID_OUTPUT, 4000, 100, 20)
                .setTankAction(SidedFluidTankComponent.Action.DRAIN));
        this.getMachineComponent().getPrimaryBar().setCanIncrease(this::canIncrease);
    }

    public void trigger() {
        this.triggered = true;
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        inputInventory.deserializeNBT(compound.getCompound("input"));
        outputFluid.readFromNBT(compound.getCompound("output"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("input", inputInventory.serializeNBT());
        compound.put("output", outputFluid.writeToNBT(new CompoundNBT()));
        return super.write(compound);
    }

    @Override
    public ResourceLocation getRecipeCategoryUID() {
        return ID;
    }

    @Override
    public PressTileEntity getSelf() {
        return this;
    }

    public InventoryComponent<PressTileEntity> getInputInventory() {
        return inputInventory;
    }

    public FluidTankComponent<PressTileEntity> getOutputFluidTank() {
        return outputFluid;
    }

    @Override
    public boolean hasInputs() {
        return !inputInventory.getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.PRESS_SERIALIZER.get().getRecipeType() && recipe instanceof PressRecipe;
    }

    @Override
    public PressRecipe castRecipe(IRecipe<?> iRecipe) {
        return (PressRecipe) iRecipe;
    }

    @Override
    public boolean matchesInputs(PressRecipe currentRecipe) {
        return this.outputFluid.fill(currentRecipe.fluidOut, IFluidHandler.FluidAction.SIMULATE) == 0 && currentRecipe.matches(inputInventory);
    }

    @Override
    public void handleComplete(PressRecipe currentRecipe) {
        if(this.hasWorld()) {
            this.getWorld().playSound(null, this.getPos(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, this.getWorld().rand.nextFloat() * 0.1F + 0.9F);
            inputInventory.getStackInSlot(0).shrink(1);
            if (currentRecipe.fluidOut != null && !currentRecipe.fluidOut.isEmpty()) {
                outputFluid.fillForced(currentRecipe.fluidOut.copy(), IFluidHandler.FluidAction.EXECUTE);
            }
            this.triggered = false;
        }
    }

    private boolean canIncrease(PressTileEntity tile) {
        if (tile.triggered && tile.hasWorld() && tile.getMachineComponent().getCurrentRecipe() != null) {
            return tile.getOutputFluidTank().getFluidAmount() + tile.getMachineComponent().getCurrentRecipe().fluidOut.getAmount() < tile.getOutputFluidTank().getCapacity();
        }
        return false;
    }
}
