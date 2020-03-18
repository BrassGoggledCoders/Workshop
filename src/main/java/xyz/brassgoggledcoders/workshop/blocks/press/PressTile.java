package xyz.brassgoggledcoders.workshop.blocks.press;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.fluid.SidedFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.recipes.PressRecipe;
import xyz.brassgoggledcoders.workshop.registries.Recipes;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.PRESS_BLOCK;

public class PressTile extends TileActive {

    @Save
    private PosProgressBar progressBar;
    @Save
    private SidedInvHandler inputInventory;
    @Save
    private SidedFluidTank outputFluid;

    private PressRecipe currentRecipe;

    public PressTile() {
        super(PRESS_BLOCK);
        this.addProgressBar(progressBar = new PosProgressBar(0, 0, 120).
                setTile(this).
                setBarDirection(PosProgressBar.BarDirection.HORIZONTAL_RIGHT).
                setCanReset(tileEntity -> true).
                setOnStart(() -> progressBar.setMaxProgress(getMaxProgress())).
                setCanIncrease(tileEntity -> canIncrease()).
                setOnFinishWork(() -> onFinish().run()));
        this.addInventory(this.inputInventory = (SidedInvHandler) new SidedInvHandler("inputInventory", 34, 25, 1, 0)
                .setColor(DyeColor.RED)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addTank(this.outputFluid = (SidedFluidTank) new SidedFluidTank("output_fluid", 4000, 149, 20, 3).
                setColor(DyeColor.MAGENTA).
                setTile(this).
                setTankAction(PosFluidTank.Action.DRAIN));

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
        return currentRecipe != null && outputFluid.fillForced(currentRecipe.fluidOut.copy(), IFluidHandler.FluidAction.SIMULATE) == currentRecipe.fluidOut.getAmount();
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            FluidStack fluidOut = outputFluid.getFluid();
            if (heldItem.isItemEqual(Items.BUCKET.getDefaultInstance())) {
                if (fluidOut.getAmount() >= 1000) {
                    ItemStack item = outputFluid.getFluid().getFluid().getFilledBucket().getDefaultInstance();
                    playerIn.inventory.addItemStackToInventory(item);
                    heldItem.shrink(1);
                    outputFluid.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            } else if (!heldItem.isEmpty()) {
                if (inputInventory.getStackInSlot(0).isEmpty()) {
                    inputInventory.insertItem(0, heldItem.copy(), false);
                    int count = heldItem.getCount();
                    heldItem.shrink(count);
                    checkForRecipe();
                    return true;
                }
            } else if(heldItem.isEmpty()) {
                ItemStack inputStack = inputInventory.getStackInSlot(0);
                if (!inputStack.isEmpty()) {
                    int count = inputStack.getCount();
                    ItemStack stack = inputInventory.extractItem(0, count, false);
                    playerIn.addItemStackToInventory(stack);
                }
                return true;
            }
        return false;
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
                        .filter(recipe -> recipe.getType() == Recipes.PRESS)
                        .map(recipe -> (PressRecipe) recipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    public SidedInvHandler getInputInventory() {
        return inputInventory;
    }

    private boolean matches(PressRecipe pressRecipe) {
        return pressRecipe.matches(inputInventory);
    }

}
