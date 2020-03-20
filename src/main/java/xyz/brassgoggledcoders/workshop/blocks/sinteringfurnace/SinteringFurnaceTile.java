package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;


import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SINTERING_FURNACE_BLOCK;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;
import xyz.brassgoggledcoders.workshop.recipes.SinteringFurnaceRecipe;
import xyz.brassgoggledcoders.workshop.registries.WorkshopRecipes;

public class SinteringFurnaceTile extends WorkshopGUIMachine {

    @Save
    private SidedInvHandler powderInventory;
    @Save
    private SidedInvHandler targetInputInventory;
    @Save
    private SidedInvHandler outputInventory;
    @Save
    private SidedInvHandler fuelInventory;

    private int burnTime = 0;

    private SinteringFurnaceRecipe currentRecipe;

    public SinteringFurnaceTile() {
        super(SINTERING_FURNACE_BLOCK, 76, 42, 100, PosProgressBar.BarDirection.HORIZONTAL_RIGHT);

        this.addInventory(this.powderInventory = (SidedInvHandler) new SidedInvHandler("powderInventory", 70, 19, 2, 0)
                .setColor(DyeColor.ORANGE)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addInventory(this.targetInputInventory = (SidedInvHandler) new SidedInvHandler("targetInputInventory", 34, 42, 1, 0)
                .setColor(DyeColor.YELLOW)
                .setTile(this)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addInventory(this.outputInventory = (SidedInvHandler) new SidedInvHandler("outputInventory", 120, 42, 1, 0)
                .setColor(DyeColor.BLACK)
                .setTile(this));
        this.addInventory(this.fuelInventory = (SidedInvHandler) new SidedInvHandler("fuelInventory", 78, 70, 1, 0)
                .setColor(DyeColor.RED)
                .setTile(this));

    }

    @Override
    public void tick() {
        super.tick();
        if(!isActive()){
            setBurnTime();
        } else{
            -- burnTime;
        }
    }

    @Override
    public boolean canIncrease() {
        return isActive() && currentRecipe != null && ItemHandlerHelper.insertItem(outputInventory, currentRecipe.itemOut.copy(), true).isEmpty();
    }

    public boolean isActive() {
        return burnTime > 0;
    }

    public int setBurnTime(){
        ItemStack stack = fuelInventory.getStackInSlot(0);
        if(!stack.isEmpty()) {
            this.burnTime = ForgeHooks.getBurnTime(stack);
            fuelInventory.getStackInSlot(0).shrink(1);
        }
        return burnTime;
    }

    @Override
    public int getMaxProgress() {
        return currentRecipe != null ? currentRecipe.meltTime : 100;
    }

    private void checkForRecipe() {
        if (isServer()) {
            if (currentRecipe == null || !currentRecipe.matches(powderInventory,targetInputInventory)) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(recipe -> recipe.getType() == WorkshopRecipes.SINTERING_FURNACE)
                        .map(recipe -> (SinteringFurnaceRecipe) recipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    private boolean matches(SinteringFurnaceRecipe sinteringFurnaceRecipe) {
        return sinteringFurnaceRecipe.matches(powderInventory, targetInputInventory);
    }


    @Override
    public Runnable onFinish() {
        return () -> {
            if (currentRecipe != null) {
                SinteringFurnaceRecipe sinteringFurnaceRecipe = currentRecipe;
                for (int i = 0; i < powderInventory.getSlots(); i++) {
                    powderInventory.getStackInSlot(i).shrink(1);
                }
                targetInputInventory.getStackInSlot(0).shrink(1);
                if (sinteringFurnaceRecipe.itemOut != null && !sinteringFurnaceRecipe.itemOut.isEmpty()) {
                    ItemHandlerHelper.insertItem(outputInventory, sinteringFurnaceRecipe.itemOut.copy(), false);
                    //checkForRecipe();
                }
            }
        };
    }

    public SidedInvHandler getFuelInventory() {
        return fuelInventory;
    }

    public SidedInvHandler getOutputInventory() {
        return outputInventory;
    }

    public SidedInvHandler getPowderInventory() {
        return powderInventory;
    }

    public SidedInvHandler getTargetInputInventory() {
        return targetInputInventory;
    }

    public SinteringFurnaceRecipe getCurrentRecipe() {
        return currentRecipe;
    }
}
