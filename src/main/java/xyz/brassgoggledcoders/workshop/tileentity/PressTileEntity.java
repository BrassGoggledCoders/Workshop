package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;

import javax.annotation.Nonnull;

public class PressTileEntity extends BasicMachineTileEntity<PressTileEntity, PressRecipe> {

    private SidedInventoryComponent<PressTileEntity> inputInventory;
    private SidedFluidTankComponent<PressTileEntity> outputFluid;

    public PressTileEntity() {
        super(WorkshopBlocks.PRESS.getTileEntityType(), new ProgressBarComponent<PressTileEntity>(0, 0, 120).
                setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.inputInventory = (SidedInventoryComponent<PressTileEntity>) new SidedInventoryComponent<PressTileEntity>("inputInventory", 34, 25, 1, pos++)
                .setColor(DyeColor.RED)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.outputFluid = (SidedFluidTankComponent<PressTileEntity>) new SidedFluidTankComponent<PressTileEntity>("output_fluid", 4000, 149, 20, pos++).
                setColor(DyeColor.MAGENTA).
                setTankAction(SidedFluidTankComponent.Action.DRAIN));
    }


    @Override
    public void read(CompoundNBT compound) {
        inputInventory.deserializeNBT(compound.getCompound("input"));
        outputFluid.readFromNBT(compound.getCompound("output"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("input", inputInventory.serializeNBT());
        compound.put("output", outputFluid.writeToNBT(new CompoundNBT()));
        return super.write(compound);
    }

    @Override
    public PressTileEntity getSelf() {
        return this;
    }

    /*@Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(hand);
        FluidStack fluidOut = outputFluid.getFluid();
        if (heldItem.isItemEqual(Items.BUCKET.getDefaultInstance())) {
            if (fluidOut.getAmount() >= 1000) {
                ItemStack item = outputFluid.getFluid().getFluid().getFilledBucket().getDefaultInstance();
                player.inventory.addItemStackToInventory(item);
                heldItem.shrink(1);
                outputFluid.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                return ActionResultType.SUCCESS;
            }
        } else if (!heldItem.isEmpty()) {
            if (inputInventory.getStackInSlot(0).isEmpty()) {
                inputInventory.insertItem(0, heldItem.copy(), false);
                int count = heldItem.getCount();
                heldItem.shrink(count);
                return ActionResultType.SUCCESS;
            }
        } else if (heldItem.isEmpty()) {
            ItemStack inputStack = inputInventory.getStackInSlot(0);
            if (!inputStack.isEmpty()) {
                int count = inputStack.getCount();
                ItemStack stack = inputInventory.extractItem(0, count, false);
                player.addItemStackToInventory(stack);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }*/

    public SidedInventoryComponent getInputInventory() {
        return inputInventory;
    }

    public SidedFluidTankComponent getOutputFluid() {
        return outputFluid;
    }

    @Override
    public boolean hasInputs() {
        return !inputInventory.getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.PRESS && recipe instanceof PressRecipe;
    }

    @Override
    public PressRecipe castRecipe(IRecipe<?> iRecipe) {
        return (PressRecipe) iRecipe;
    }

    @Override
    public int getProcessingTime(PressRecipe currentRecipe) {
        return 6 * 20;
    }

    @Override
    public boolean matchesInputs(PressRecipe currentRecipe) {
        return currentRecipe.matches(inputInventory);
    }

    @Override
    public void handleComplete(PressRecipe currentRecipe) {
        inputInventory.getStackInSlot(0).shrink(1);
        outputFluid.fillForced(currentRecipe.fluidOut.copy(), IFluidHandler.FluidAction.EXECUTE);
    }
}
