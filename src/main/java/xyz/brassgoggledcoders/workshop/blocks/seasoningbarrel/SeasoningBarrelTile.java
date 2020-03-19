package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SEASONING_BARREL_BLOCK;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.fluid.SidedFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;

import net.minecraft.item.DyeColor;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;
import xyz.brassgoggledcoders.workshop.recipes.SeasoningBarrelRecipe;
import xyz.brassgoggledcoders.workshop.registries.Recipes;

public class SeasoningBarrelTile extends WorkshopGUIMachine {

    @Save
    private SidedInvHandler inputInventory;
    @Save
    private SidedFluidTank inputFluidTank;
    @Save
    private SidedInvHandler outputInventory;
    @Save
    private SidedFluidTank outputFluidTank;

    private SeasoningBarrelRecipe currentRecipe;

    public SeasoningBarrelTile() {
        super(SEASONING_BARREL_BLOCK, 76, 42, 100, PosProgressBar.BarDirection.HORIZONTAL_RIGHT);
        this.addInventory(this.inputInventory = (SidedInvHandler) new SidedInvHandler("inputInventory", 29, 42, 1, 0)
                .setColor(DyeColor.LIGHT_BLUE).setTile(this).setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addTank(this.inputFluidTank = (SidedFluidTank) new SidedFluidTank("inputFluidTank", 4000, 52, 20, 1)
                .setColor(DyeColor.BROWN).setTile(this).setTankAction(PosFluidTank.Action.FILL)
                .setOnContentChange(this::checkForRecipe));
        this.addInventory(this.outputInventory = (SidedInvHandler) new SidedInvHandler("outputInventory", 130, 42, 1, 2)
                .setColor(DyeColor.BLUE).setInputFilter((stack, integer) -> false).setTile(this));
        this.addTank(this.outputFluidTank = (SidedFluidTank) new SidedFluidTank("outputFluidTank", 4000, 105, 20, 1)
                .setColor(DyeColor.BLACK).setTile(this).setTankAction(PosFluidTank.Action.DRAIN));
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
                    outputInventory.insertItem(0, seasoningBarrelRecipe.itemOut, false);
                }
            }
        };
    }

    private void checkForRecipe() {
        if(isServer()) {
            if(currentRecipe == null || !currentRecipe.matches(inputInventory, inputFluidTank)) {
                currentRecipe = this.getWorld().getRecipeManager().getRecipes().stream()
                        .filter(recipe -> recipe.getType() == Recipes.SEASONING_BARREL)
                        .map(recipe -> (SeasoningBarrelRecipe) recipe).filter(this::matches).findFirst().orElse(null);
            }
        }
    }

    private boolean matches(SeasoningBarrelRecipe seasoningBarrelRecipe) {
        Workshop.LOGGER.info(seasoningBarrelRecipe.matches(inputInventory, inputFluidTank));
        return seasoningBarrelRecipe.matches(inputInventory, inputFluidTank);
    }
}
