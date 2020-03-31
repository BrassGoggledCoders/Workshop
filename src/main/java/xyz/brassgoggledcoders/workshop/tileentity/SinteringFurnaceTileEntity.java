package xyz.brassgoggledcoders.workshop.tileentity;


import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SinteringFurnaceTileEntity extends BasicMachineTileEntity<SinteringFurnaceTileEntity, SinteringFurnaceRecipe> {

    private SidedInventoryComponent<SinteringFurnaceTileEntity> powderInventory;
    private SidedInventoryComponent<SinteringFurnaceTileEntity> targetInputInventory;
    private SidedInventoryComponent<SinteringFurnaceTileEntity> outputInventory;
    private SidedInventoryComponent<SinteringFurnaceTileEntity> fuelInventory;

    private int burnTime = 0;

    private SinteringFurnaceRecipe currentRecipe;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public SinteringFurnaceTileEntity() {
        super(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(),
                new ProgressBarComponent<SinteringFurnaceTileEntity>(76, 42, 100)
                        .setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.powderInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("powderInventory", 70, 19, 2, pos++)
                .setColor(DyeColor.ORANGE)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.getMachineComponent().addInventory(this.targetInputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("targetInputInventory", 34, 42, 1, pos++)
                .setColor(DyeColor.YELLOW)
                .setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.getMachineComponent().addInventory(this.outputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("outputInventory", 120, 42, 1, pos++)
                .setColor(DyeColor.BLACK));
        this.getMachineComponent().addInventory(this.fuelInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("fuelInventory", 78, 70, 1, pos++)
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
            handleBurnTime();
        } else {
            --burnTime;
        }
    }

    @Override
    public SinteringFurnaceTileEntity getSelf() {
        return this;
    }

    public boolean isActive() {
        return burnTime > 0;
    }

    public void handleBurnTime() {
        ItemStack stack = fuelInventory.getStackInSlot(0);
        if (!stack.isEmpty()) {
            this.burnTime = ForgeHooks.getBurnTime(stack);
            fuelInventory.getStackInSlot(0).shrink(1);
        }
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

    public SidedInventoryComponent<SinteringFurnaceTileEntity> getOutputInventory() {
        return outputInventory;
    }

    public SidedInventoryComponent<SinteringFurnaceTileEntity> getPowderInventory() {
        return powderInventory;
    }

    public SidedInventoryComponent<SinteringFurnaceTileEntity> getTargetInputInventory() {
        return targetInputInventory;
    }

    public SinteringFurnaceRecipe getCurrentRecipe() {
        return currentRecipe;
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
    public SinteringFurnaceRecipe castRecipe(IRecipe<?> iRecipe) {
        return null;
    }

    @Override
    public int getProcessingTime(SinteringFurnaceRecipe currentRecipe) {
        return 0;
    }

    @Override
    public boolean matchesInputs(SinteringFurnaceRecipe currentRecipe) {
        return false;
    }

    @Override
    public void handleComplete(SinteringFurnaceRecipe currentRecipe) {
        for (int i = 0; i < powderInventory.getSlots(); i++) {
            powderInventory.getStackInSlot(i).shrink(1);
        }
        targetInputInventory.getStackInSlot(0).shrink(1);
        if (currentRecipe.itemOut != null && !currentRecipe.itemOut.isEmpty()) {
            ItemHandlerHelper.insertItem(outputInventory, currentRecipe.itemOut.copy(), false);
        }
    }
}
