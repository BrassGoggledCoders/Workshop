package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;
import xyz.brassgoggledcoders.workshop.recipes.SeasoningBarrelRecipe;
import xyz.brassgoggledcoders.workshop.registries.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.registries.WorkshopRecipes;

import javax.annotation.Nonnull;

public class SeasoningBarrelTile extends WorkshopGUIMachine<SeasoningBarrelTile> {

    private static final int tankSize = 4000; // mB

    private SidedInventoryComponent<SeasoningBarrelTile> inputInventory;
    private SidedFluidTankComponent<SeasoningBarrelTile> inputFluidTank;
    private SidedInventoryComponent<SeasoningBarrelTile> outputInventory;
    private SidedFluidTankComponent<SeasoningBarrelTile> outputFluidTank;

    private SeasoningBarrelRecipe currentRecipe;

    public SeasoningBarrelTile() {
        super(WorkshopBlocks.SEASONING_BARREL.getBlock(), 76, 42, 100, ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT);
        this.addInventory(this.inputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("inputInventory", 29, 42, 1, 0)
                .setColor(DyeColor.LIGHT_BLUE)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addTank(this.inputFluidTank = (SidedFluidTankComponent) new SidedFluidTankComponent("inputFluidTank", tankSize, 52, 20, 1)
                .setColor(DyeColor.BROWN)
                .setTankAction(SidedFluidTankComponent.Action.FILL)
                .setOnContentChange(this::checkForRecipe));
        this.addInventory(this.outputInventory = (SidedInventoryComponent) new SidedInventoryComponent("outputInventory", 130, 42, 1, 2)
                .setColor(DyeColor.BLUE)
                .setInputFilter((stack, integer) -> false));
        this.addTank(this.outputFluidTank = (SidedFluidTankComponent) new SidedFluidTankComponent("outputFluidTank", tankSize, 105, 20, 1)
                .setColor(DyeColor.BLACK)
                .setTankAction(SidedFluidTankComponent.Action.DRAIN));
    }

    @Override
    public void read(CompoundNBT compound) {
        inputInventory.deserializeNBT(compound.getCompound("inputInventory"));
        inputFluidTank.readFromNBT(compound.getCompound("inputFluidTank"));
        outputInventory.deserializeNBT(compound.getCompound("outputInventory"));
        outputFluidTank.readFromNBT(compound.getCompound("outputFluidTank"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inputInventory", inputInventory.serializeNBT());
        compound.put("inputFluidTank", inputFluidTank.writeToNBT(new CompoundNBT()));
        compound.put("outputInventory", outputInventory.serializeNBT());
        compound.put("outputFluidTank", outputFluidTank.writeToNBT(new CompoundNBT()));
        return super.write(compound);
    }

    @Override
    public boolean canIncrease() {
        return currentRecipe != null
                && ItemHandlerHelper.insertItem(outputInventory, currentRecipe.itemOut.copy(), true).isEmpty()
                && outputFluidTank.fillForced(currentRecipe.fluidOut.copy(),
                        IFluidHandler.FluidAction.SIMULATE) == currentRecipe.fluidOut.getAmount();
    }

    @Override
    public int getMaxProgress() {
        return currentRecipe != null ? currentRecipe.seasoningTime : 1000;
    }

    @Override
    public Runnable onFinish() {
        return () -> {
            if(currentRecipe != null) {
                SeasoningBarrelRecipe seasoningBarrelRecipe = currentRecipe;
                inputFluidTank.drainForced(seasoningBarrelRecipe.fluidInput, IFluidHandler.FluidAction.EXECUTE);
                for(int i = 0; i < inputInventory.getSlots(); i++) {
                    int count = seasoningBarrelRecipe.itemIn.getCount();
                    inputInventory.getStackInSlot(i).shrink(count);
                }
                if(outputFluidTank.getFluid().equals(seasoningBarrelRecipe.fluidOut) || outputFluidTank.isEmpty()) {
                    int capacity = outputFluidTank.getCapacity();
                    if(capacity >= outputFluidTank.getFluid().getAmount()
                            + seasoningBarrelRecipe.fluidOut.getAmount()) {
                        outputFluidTank.fillForced(seasoningBarrelRecipe.fluidOut.copy(),
                                IFluidHandler.FluidAction.EXECUTE);
                    }
                }
                for(int i = 0; i < outputInventory.getSlots(); i++) {
                    ItemStack itemOut = seasoningBarrelRecipe.itemOut;
                    if(itemOut != null) {
                        outputInventory.insertItem(0, itemOut, false);
                    }
                }
            }
        };
    }

    private void checkForRecipe() {
        if(isServer()) {
            if(currentRecipe == null || !currentRecipe.matches(inputInventory, inputFluidTank)) {
                currentRecipe = this.getWorld().getRecipeManager().getRecipes().stream()
                        .filter(recipe -> recipe.getType() == WorkshopRecipes.SEASONING_BARREL)
                        .map(recipe -> (SeasoningBarrelRecipe) recipe).filter(recipe -> recipe.matches(inputInventory, inputFluidTank)).findFirst().orElse(null);
            }
        }
    }

    @Nonnull
    @Override
    public SeasoningBarrelTile getSelf() {
        return this;
    }
}
