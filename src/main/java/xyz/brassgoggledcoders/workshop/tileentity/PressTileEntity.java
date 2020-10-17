package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.jei.PressRecipeCategory;
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class PressTileEntity extends BasicMachineTileEntity<PressTileEntity, PressRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.PRESS_SERIALIZER.get().getRecipeType().toString());

    private final InventoryComponent<PressTileEntity> inputInventory;
    private final FluidTankComponent<PressTileEntity> outputFluid;

    private double height = 0.8;

    public PressTileEntity() {
        super(WorkshopBlocks.PRESS.getTileEntityType(), new ProgressBarComponent<PressTileEntity>(70, 40, 120).
                setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.inputInventory = new SidedInventoryComponent<PressTileEntity>(InventoryUtil.ITEM_INPUT, 45, 50, 1, pos++)
                .setColor(InventoryUtil.ITEM_INPUT_COLOR)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.outputFluid = new SidedFluidTankComponent<PressTileEntity>(InventoryUtil.FLUID_OUTPUT, 4000, 100, 20, pos++).
                setColor(InventoryUtil.FLUID_OUTPUT_COLOR).
                setTankAction(SidedFluidTankComponent.Action.DRAIN));
        this.getMachineComponent().getPrimaryBar().setCanIncrease(this::canIncrease);
    }

    @Override
    public void tick() {
        if(world != null) {
            if (world.getGameTime() % 5 == 0) {
                updatePressProgress();
            }
            setHeightChange();
        }

        super.tick();
    }

    public void setHeightChange(){
        int progress = getMachineComponent().getPrimaryBar().getProgress();
        double maxHeigh = 0.8;
        if(progress == 0){
            height = maxHeigh;
        } else{
            int max = getMachineComponent().getPrimaryBar().getMaxProgress();
            int bottom = max/2;
            double minHeigh = 0.3;
            if(progress >= bottom){
                height = minHeigh;
                if(progress != bottom) {
                    progress = progress - bottom;
                    float sections = (float)((maxHeigh - minHeigh) /bottom);
                    float offset = (float)progress*sections;
                    height = Math.min(maxHeigh,height + offset);
                }
            } else {
                height = maxHeigh;
                float sections = (float)((maxHeigh - minHeigh) /bottom);
                float offset = (float)progress*sections;
                height = Math.max(height - offset, minHeigh);
            }

        }
    }

    public double getHeightChange() {
        return height;
    }

    public void updatePressProgress() {
        requestModelDataUpdate();
        this.markDirty();
        if (this.getWorld() != null) {
            this.getWorld().notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        deserializeNBT(tag);
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

    public InventoryComponent<PressTileEntity> getInputInventory() {
        return inputInventory;
    }

    public FluidTankComponent<PressTileEntity> getOutputFluid() {
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
        if (currentRecipe.fluidOut != null && !currentRecipe.fluidOut.isEmpty()) {
            outputFluid.fillForced(currentRecipe.fluidOut.copy(), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private boolean canIncrease(PressTileEntity tile) {
        if (world == null) {
            return false;
        }
        if (!world.isBlockPowered(pos) && !world.isBlockPowered(pos.up())) {
            return false;
        }
        if (getMachineComponent().getCurrentRecipe() == null) {
            return false;
        }
        return tile.getOutputFluid().getCapacity() != tile.getOutputFluid().getFluidAmount() && tile.getOutputFluid().getFluidAmount() + tile.getMachineComponent().getCurrentRecipe().fluidOut.getAmount() < tile.getOutputFluid().getCapacity();
    }


}
