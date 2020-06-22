package xyz.brassgoggledcoders.workshop.tileentity;


import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.block.SinteringFurnaceBlock;
import xyz.brassgoggledcoders.workshop.component.machine.BurnTimerComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class SinteringFurnaceTileEntity extends BasicMachineTileEntity<SinteringFurnaceTileEntity, SinteringFurnaceRecipe> {

    private final SidedInventoryComponent<SinteringFurnaceTileEntity> powderInventory;
    private final SidedInventoryComponent<SinteringFurnaceTileEntity> inputInventory;
    private final SidedInventoryComponent<SinteringFurnaceTileEntity> outputInventory;
    private final SidedInventoryComponent<SinteringFurnaceTileEntity> fuelInventory;
    private final BurnTimerComponent<SinteringFurnaceTileEntity> burnTimer;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public SinteringFurnaceTileEntity() {
        super(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(),
                new ProgressBarComponent<SinteringFurnaceTileEntity>(76, 42, 100)
                        .setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.powderInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("powderInventory", 70, 19, 2, pos++)
                .setColor(DyeColor.ORANGE)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.inputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>(InventoryUtil.ITEM_INPUT, 34, 42, 1, pos++)
                .setColor(InventoryUtil.ITEM_INPUT_COLOR)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.outputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>(InventoryUtil.ITEM_OUTPUT, 120, 42, 1, pos++)
                .setColor(InventoryUtil.ITEM_OUTPUT_COLOR));
        this.getMachineComponent().addProgressBar(this.burnTimer = new BurnTimerComponent<>(60, 60, 0));
        this.getMachineComponent().getPrimaryBar().setCanIncrease(this::canIncrease);
        this.getMachineComponent().addInventory(this.fuelInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("fuelInventory", 78, 70, 1, pos++)
                .setColor(DyeColor.BLACK)
                .setOnSlotChanged((stack, slot) -> {
                    if (burnTimer.getProgress() <= 0) {
                        int time = handleBurnTime();
                        this.burnTimer.setMaxProgress(time);
                        this.burnTimer.setProgress(time);
                    }
                }));
        this.burnTimer.setOnStart(() -> this.getWorld().setBlockState(this.pos, this.world.getBlockState(this.pos).with(SinteringFurnaceBlock.LIT, true), 3));
        this.burnTimer.setOnFinishWork(() -> {
            int time = handleBurnTime();
            if (time == 0) {
                this.burnTimer.setProgress(time);
                this.burnTimer.setMaxProgress(time);
                this.getWorld().setBlockState(this.pos, this.world.getBlockState(this.pos).with(SinteringFurnaceBlock.LIT, false), 3);
            } else if(burnTimer.getMaxProgress() != time){
                this.burnTimer.setProgress(time);
                this.burnTimer.setMaxProgress(time);
            }
        });
    }

    @Override
    public void read(CompoundNBT compound) {
        powderInventory.deserializeNBT(compound.getCompound("powderInventory"));
        inputInventory.deserializeNBT(compound.getCompound("targetInputInventory"));
        outputInventory.deserializeNBT(compound.getCompound("outputInventory"));
        fuelInventory.deserializeNBT(compound.getCompound("fuelInventory"));
        burnTimer.deserializeNBT(compound.getCompound("burnTimer"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("powderInventory", powderInventory.serializeNBT());
        compound.put("targetInputInventory", inputInventory.serializeNBT());
        compound.put("outputInventory", outputInventory.serializeNBT());
        compound.put("fuelInventory", fuelInventory.serializeNBT());
        compound.put("burnTimer", burnTimer.serializeNBT());
        return super.write(compound);
    }

    @Override
    public SinteringFurnaceTileEntity getSelf() {
        return this;
    }

    public int handleBurnTime() {
        ItemStack stack = fuelInventory.getStackInSlot(0).copy();
        if (!stack.isEmpty()) {
            fuelInventory.getStackInSlot(0).shrink(1);
            return ForgeHooks.getBurnTime(stack);
        }
        return 0;
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

    @Override
    public boolean hasInputs() {
        return !inputInventory.getStackInSlot(0).isEmpty() && !powderInventory.getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.SINTERING_FURNACE_SERIALIZER.get().getRecipeType() && recipe instanceof SinteringFurnaceRecipe;
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
        return ItemHandlerHelper.insertItemStacked(this.outputInventory, currentRecipe.itemOut, true).isEmpty() && currentRecipe.matches(inputInventory, powderInventory);
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

    private boolean canIncrease(SinteringFurnaceTileEntity tile) {
        return tile.burnTimer.getProgress() > 0 && !tile.getInputInventory().getStackInSlot(0).isEmpty() && !tile.getPowderInventory().getStackInSlot(0).isEmpty() || tile.burnTimer.getProgress() > 0 && !tile.getInputInventory().getStackInSlot(0).isEmpty() && !tile.getPowderInventory().getStackInSlot(1).isEmpty();
    }
}
