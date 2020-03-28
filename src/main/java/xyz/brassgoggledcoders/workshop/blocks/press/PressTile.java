package xyz.brassgoggledcoders.workshop.blocks.press;

import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.recipes.PressRecipe;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;

import javax.annotation.Nonnull;

public class PressTile extends ActiveTile<PressTile> {

    private ProgressBarComponent<PressTile> progressBar;
    private SidedInventoryComponent<PressTile> inputInventory;
    private SidedFluidTankComponent<PressTile> outputFluid;

    private PressRecipe currentRecipe;

    public PressTile() {
        super(WorkshopBlocks.PRESS.getTileEntityType(), WorkshopBlocks.PRESS.getBlock());
        this.addProgressBar(progressBar = new ProgressBarComponent(0, 0, 120).
                setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT).
                setCanReset(tileEntity -> true).
                setOnStart(() -> progressBar.setMaxProgress(getMaxProgress())).
                setCanIncrease(tileEntity -> canIncrease()).
                setOnFinishWork(() -> onFinish().run()));
        this.addInventory(this.inputInventory = (SidedInventoryComponent) new SidedInventoryComponent("inputInventory", 34, 25, 1, 0)
                .setColor(DyeColor.RED)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addTank(this.outputFluid = (SidedFluidTankComponent) new SidedFluidTankComponent("output_fluid", 4000, 149, 20, 3).
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

    private Runnable onFinish() {
        return () -> {
            if (currentRecipe != null) {
                PressRecipe pressRecipes = currentRecipe;
                int count = currentRecipe.itemIn.getCount();
                inputInventory.getStackInSlot(0).shrink(count);
                outputFluid.fillForced(pressRecipes.fluidOut.copy(), IFluidHandler.FluidAction.EXECUTE);
            }
            checkForRecipe();
        };
    }


    public boolean canIncrease() {
        return currentRecipe != null && outputFluid.fillForced(currentRecipe.fluidOut.copy(), IFluidHandler.FluidAction.SIMULATE) == currentRecipe.fluidOut.getAmount() && isActive();
    }

    @Override
    public ActionResultType onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            FluidStack fluidOut = outputFluid.getFluid();
            if (heldItem.isItemEqual(Items.BUCKET.getDefaultInstance())) {
                if (fluidOut.getAmount() >= 1000) {
                    ItemStack item = outputFluid.getFluid().getFluid().getFilledBucket().getDefaultInstance();
                    playerIn.inventory.addItemStackToInventory(item);
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
            } else if(heldItem.isEmpty()) {
                ItemStack inputStack = inputInventory.getStackInSlot(0);
                if (!inputStack.isEmpty()) {
                    int count = inputStack.getCount();
                    ItemStack stack = inputInventory.extractItem(0, count, false);
                    playerIn.addItemStackToInventory(stack);
                }
                return ActionResultType.SUCCESS;
            }
        return ActionResultType.PASS;
    }

    @Nonnull
    @Override
    public PressTile getSelf() {
        return this;
    }

    public int getMaxProgress() {
        return 120;
    }

    private void checkForRecipe() {
        if (isServer()) {
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

    public boolean isActive(){
        return true;
    }

}
