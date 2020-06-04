package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PressTileEntity extends BasicMachineTileEntity<PressTileEntity, PressRecipe> {

    private final SidedInventoryComponent<PressTileEntity> inputInventory;
    private final SidedFluidTankComponent<PressTileEntity> outputFluid;

    private double height = 0.8;

    public PressTileEntity() {
        super(WorkshopBlocks.PRESS.getTileEntityType(), new ProgressBarComponent<PressTileEntity>(70, 40, 120).
                setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.inputInventory = (SidedInventoryComponent<PressTileEntity>) new SidedInventoryComponent<PressTileEntity>(InventoryUtil.ITEM_INPUT, 45, 50, 1, pos++)
                .setColor(InventoryUtil.ITEM_INPUT_COLOR)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.outputFluid = (SidedFluidTankComponent<PressTileEntity>) new SidedFluidTankComponent<PressTileEntity>(InventoryUtil.FLUID_OUTPUT, 4000, 100, 20, pos++).
                setColor(InventoryUtil.FLUID_OUTPUT_COLOR).
                setTankAction(SidedFluidTankComponent.Action.DRAIN));
    }

    @Override
    public void tick() {
        int progress = getMachineComponent().getPrimaryBar().getProgress();
        int max = getMachineComponent().getPrimaryBar().getMaxProgress();
        double time = (double) max / 60;
        updatePressProgress();
        if (progress % time == 0 && progress != 0) {
            if (height > 0.2) {
                height = height - 0.01;
            }
        } else if (progress == 0 && height != 0.8) {
            height = 0.8;
        }
        super.tick();
    }

    public double getHeight() {
        return height;
    }

    public void updatePressProgress() {
        requestModelDataUpdate();
        this.markDirty();
        if (this.getWorld() != null) {
            this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), -1, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = new CompoundNBT();
        updateTag.putDouble("height", getHeight());
        getInputInventory().getStackInSlot(0).write(updateTag);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        height = tag.getDouble("height");
        if (getInputInventory().getStackInSlot(0).isEmpty()) {
            getInputInventory().insertItem(0, ItemStack.read(tag), false);
        }
        updatePressProgress();
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

    public SidedInventoryComponent<PressTileEntity> getInputInventory() {
        return inputInventory;
    }

    public SidedFluidTankComponent<PressTileEntity> getOutputFluid() {
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
    public int getProcessingTime(PressRecipe currentRecipe) {
        return 6 * 20;
    }

    @Override
    public boolean matchesInputs(PressRecipe currentRecipe) {
        return this.outputFluid.fill(currentRecipe.fluidOut, IFluidHandler.FluidAction.SIMULATE) == 0 && currentRecipe.matches(inputInventory);
    }

    @Override
    public void handleComplete(PressRecipe currentRecipe) {
        inputInventory.getStackInSlot(0).shrink(1);
        outputFluid.fillForced(currentRecipe.fluidOut.copy(), IFluidHandler.FluidAction.EXECUTE);
    }
}
