package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;


import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipes.SinteringFurnaceRecipe;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SinteringFurnaceTile extends WorkshopGUIMachine<SinteringFurnaceTile> {

    private SidedInventoryComponent<SinteringFurnaceTile> powderInventory;
    private SidedInventoryComponent<SinteringFurnaceTile> targetInputInventory;
    private SidedInventoryComponent<SinteringFurnaceTile> outputInventory;
    private SidedInventoryComponent<SinteringFurnaceTile> fuelInventory;

    private int burnTime = 0;

    private SinteringFurnaceRecipe currentRecipe;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public SinteringFurnaceTile() {
        super(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(),
                new ProgressBarComponent<SinteringFurnaceTile>(76, 42, 100)
                        .setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        this.getMachineComponent().addInventory(this.powderInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("powderInventory", 70, 19, 2, 0)
                .setColor(DyeColor.ORANGE)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.getMachineComponent().addInventory(this.targetInputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("targetInputInventory", 34, 42, 1, 0)
                .setColor(DyeColor.YELLOW)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.getMachineComponent().addInventory(this.outputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("outputInventory", 120, 42, 1, 0)
                .setColor(DyeColor.BLACK));
        this.getMachineComponent().addInventory(this.fuelInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("fuelInventory", 78, 70, 1, 0)
                .setColor(DyeColor.RED));
    }

    @Override
    public void read(CompoundNBT compound) {
        powderInventory.deserializeNBT(compound.getCompound("powderInventory"));
        targetInputInventory.deserializeNBT(compound.getCompound("targetInputInventory"));
        outputInventory.deserializeNBT(compound.getCompound("outputInventory"));
        fuelInventory.deserializeNBT(compound.getCompound("fuelInventory"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("powderInventory", powderInventory.serializeNBT());
        compound.put("targetInputInventory", targetInputInventory.serializeNBT());
        compound.put("outputInventory", outputInventory.serializeNBT());
        compound.put("fuelInventory", fuelInventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void tick() {
        if (!isActive()) {
            setBurnTime();
        } else {
            --burnTime;
        }
    }

    @Override
    public boolean canIncrease() {
        return isActive() && currentRecipe != null && ItemHandlerHelper.insertItem(outputInventory, currentRecipe.itemOut.copy(), true).isEmpty();
    }

    public boolean isActive() {
        return burnTime > 0;
    }

    public int setBurnTime() {
        ItemStack stack = fuelInventory.getStackInSlot(0);
        if (!stack.isEmpty()) {
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
        if (!Objects.requireNonNull(this.getWorld()).isRemote()) {
            if (currentRecipe == null || !currentRecipe.matches(powderInventory, targetInputInventory)) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(recipe -> recipe.getType() == WorkshopRecipes.SINTERING_FURNACE)
                        .map(recipe -> (SinteringFurnaceRecipe) recipe)
                        .filter(recipe -> recipe.matches(powderInventory, targetInputInventory))
                        .findFirst()
                        .orElse(null);
            }
        }
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
                }
            }
        };
    }

    public SidedInventoryComponent<SinteringFurnaceTile> getOutputInventory() {
        return outputInventory;
    }

    public SidedInventoryComponent<SinteringFurnaceTile> getPowderInventory() {
        return powderInventory;
    }

    public SidedInventoryComponent<SinteringFurnaceTile> getTargetInputInventory() {
        return targetInputInventory;
    }

    public SinteringFurnaceRecipe getCurrentRecipe() {
        return currentRecipe;
    }
}
