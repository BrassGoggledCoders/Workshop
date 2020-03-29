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
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;

import javax.annotation.Nonnull;

public class PressTileEntity extends BasicMachineTileEntity<PressTileEntity, PressRecipe> {

    private ProgressBarComponent<PressTileEntity> progressBar;
    private SidedInventoryComponent<PressTileEntity> inputInventory;
    private SidedFluidTankComponent<PressTileEntity> outputFluid;

    private PressRecipe currentRecipe;

    public PressTileEntity() {
        super(WorkshopBlocks.PRESS.getTileEntityType(), new ProgressBarComponent(0, 0, 120).
                setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        this.getMachineComponent().addInventory(this.inputInventory = (SidedInventoryComponent) new SidedInventoryComponent("inputInventory", 34, 25, 1, 0)
                .setColor(DyeColor.RED)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.getMachineComponent().addTank(this.outputFluid = (SidedFluidTankComponent) new SidedFluidTankComponent("output_fluid", 4000, 149, 20, 3).
                setColor(DyeColor.MAGENTA).
                setTankAction(SidedFluidTankComponent.Action.DRAIN));
    }


    @Override
    public void read(CompoundNBT compound) {
        progressBar.deserializeNBT(compound.getCompound("progress"));
        inputInventory.deserializeNBT(compound.getCompound("input"));
        outputFluid.readFromNBT(compound.getCompound("output"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("progress", progressBar.serializeNBT());
        compound.put("input", inputInventory.serializeNBT());
        compound.put("output", outputFluid.writeToNBT(new CompoundNBT()));
        return super.write(compound);
    }

    @Override
    public PressTileEntity getSelf() {
        return this;
    }


    public boolean canIncrease() {
        return currentRecipe != null && outputFluid.fillForced(currentRecipe.fluidOut.copy(), IFluidHandler.FluidAction.SIMULATE) == currentRecipe.fluidOut.getAmount() && isActive();
    }

    @Override
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
                checkForRecipe();
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
    }

    private void checkForRecipe() {
        if (!this.getWorld().isRemote) {
            if (currentRecipe == null || !currentRecipe.matches(inputInventory)) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(recipe -> recipe.getType() == WorkshopRecipes.PRESS)
                        .map(recipe -> (PressRecipe) recipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    public SidedInventoryComponent getInputInventory() {
        return inputInventory;
    }

    public SidedFluidTankComponent getOutputFluid() {
        return outputFluid;
    }

    private boolean matches(PressRecipe pressRecipe) {
        return pressRecipe.matches(inputInventory);
    }

    public boolean isActive() {
        return true;
    }

    @Override
    public boolean hasInputs() {
        return false;
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return false;
    }

    @Override
    public PressRecipe castRecipe(IRecipe<?> iRecipe) {
        return null;
    }

    @Override
    public int getProcessingTime(PressRecipe currentRecipe) {
        return 0;
    }

    @Override
    public boolean matchesInputs(PressRecipe currentRecipe) {
        return false;
    }

    @Override
    public void handleComplete(PressRecipe currentRecipe) {
        int count = currentRecipe.itemIn.getCount();
        inputInventory.getStackInSlot(0).shrink(count);
        outputFluid.fillForced(currentRecipe.fluidOut.copy(), IFluidHandler.FluidAction.EXECUTE);
    }
}
