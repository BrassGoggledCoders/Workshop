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
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;

import javax.annotation.Nonnull;

public class SinteringFurnaceTileEntity extends BasicMachineTileEntity<SinteringFurnaceTileEntity, SinteringFurnaceRecipe> {

    private SidedInventoryComponent<SinteringFurnaceTileEntity> powderInventory;
    private SidedInventoryComponent<SinteringFurnaceTileEntity> inputInventory;
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
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.inputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("targetInputInventory", 34, 42, 1, pos++)
                .setColor(DyeColor.YELLOW)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.outputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("outputInventory", 120, 42, 1, pos++)
                .setColor(DyeColor.BLACK));
        this.getMachineComponent().addInventory(this.fuelInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("fuelInventory", 78, 70, 1, pos++)
                .setColor(DyeColor.RED));
    }

    @Override
    public void read(CompoundNBT compound) {
        powderInventory.deserializeNBT(compound.getCompound("powderInventory"));
        inputInventory.deserializeNBT(compound.getCompound("targetInputInventory"));
        outputInventory.deserializeNBT(compound.getCompound("outputInventory"));
        fuelInventory.deserializeNBT(compound.getCompound("fuelInventory"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("powderInventory", powderInventory.serializeNBT());
        compound.put("targetInputInventory", inputInventory.serializeNBT());
        compound.put("outputInventory", outputInventory.serializeNBT());
        compound.put("fuelInventory", fuelInventory.serializeNBT());
        return super.write(compound);
    }

    //TODO Refactor this into machine component
    @Override
    public void tick() {
        if (!isActive()) {
            handleBurnTime();
        } else {
            --burnTime;
        }
        super.tick();
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

    public SidedInventoryComponent<SinteringFurnaceTileEntity> getOutputInventory() {
        return outputInventory;
    }

    public SidedInventoryComponent<SinteringFurnaceTileEntity> getPowderInventory() {
        return powderInventory;
    }

    public SidedInventoryComponent<SinteringFurnaceTileEntity> getInputInventory() {
        return inputInventory;
    }

    public SinteringFurnaceRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    @Override
    public boolean hasInputs() {
        return !inputInventory.getStackInSlot(0).isEmpty() && !powderInventory.getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.SINTERING_FURNACE && recipe instanceof SinteringFurnaceRecipe;
    }

    @Override
    public SinteringFurnaceRecipe castRecipe(IRecipe<?> iRecipe) {
        return (SinteringFurnaceRecipe) iRecipe;
    }

    @Override
    public int getProcessingTime(SinteringFurnaceRecipe currentRecipe) {
        return currentRecipe.getProcessingTime();
    }

    @Override
    public boolean matchesInputs(SinteringFurnaceRecipe currentRecipe) {
        return currentRecipe.matches(inputInventory, powderInventory);
    }

    @Override
    public void handleComplete(SinteringFurnaceRecipe currentRecipe) {
        for (int i = 0; i < powderInventory.getSlots(); i++) {
            powderInventory.getStackInSlot(i).shrink(1);
        }
        inputInventory.getStackInSlot(0).shrink(1);
        if (currentRecipe.itemOut != null && !currentRecipe.itemOut.isEmpty()) {
            ItemHandlerHelper.insertItem(outputInventory, currentRecipe.itemOut.copy(), false);
        }
    }
}
