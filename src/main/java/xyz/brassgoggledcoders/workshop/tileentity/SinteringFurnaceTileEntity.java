package xyz.brassgoggledcoders.workshop.tileentity;


import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.WorkshopBlockTags;
import xyz.brassgoggledcoders.workshop.block.SinteringFurnaceBlock;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class SinteringFurnaceTileEntity extends BasicMachineTileEntity<SinteringFurnaceTileEntity, SinteringFurnaceRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.SINTERING_FURNACE_SERIALIZER.get().getRecipeType().toString());
    private final SidedInventoryComponent<SinteringFurnaceTileEntity> powderInventory;
    private final SidedInventoryComponent<SinteringFurnaceTileEntity> inputInventory;
    private final SidedInventoryComponent<SinteringFurnaceTileEntity> outputInventory;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public SinteringFurnaceTileEntity() {
        super(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(),
                new ProgressBarComponent<SinteringFurnaceTileEntity>(76, 42, 100)
                        .setBarDirection(ProgressBarComponent.BarDirection.ARROW_RIGHT));
        int pos = 0;
        //TODO Prevent insertion by hand
        this.getMachineComponent().addInventory(this.powderInventory = (SidedInventoryComponent) new SidedInventoryComponent<>("powderInventory", 70, 19, 1, pos++)
                .setColor(DyeColor.ORANGE)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.inputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>(InventoryUtil.ITEM_INPUT, 34, 42, 1, pos++)
                .setColor(InventoryUtil.ITEM_INPUT_COLOR)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.outputInventory = (SidedInventoryComponent) new SidedInventoryComponent<>(InventoryUtil.ITEM_OUTPUT, 120, 42, 1, pos++)
                .setColor(InventoryUtil.ITEM_OUTPUT_COLOR));
        this.getMachineComponent().getPrimaryBar().setCanIncrease((tile) -> hasHeat() && tile.hasInputs() && tile.getMachineComponent().getCurrentRecipe() != null);
        this.powderInventory.disableFacingAddon();
    }

    //FIXME Efficiency. Cache state checks
    @Override
    public void tick() {
        if(this.getBlockState().get(SinteringFurnaceBlock.LIT) != this.hasHeat()) {
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(SinteringFurnaceBlock.LIT, hasHeat()), 3);
            this.markDirty();
            this.markComponentDirty();
        }
        super.tick();
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        powderInventory.deserializeNBT(compound.getCompound("powderInventory"));
        inputInventory.deserializeNBT(compound.getCompound("targetInputInventory"));
        outputInventory.deserializeNBT(compound.getCompound("outputInventory"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("powderInventory", powderInventory.serializeNBT());
        compound.put("targetInputInventory", inputInventory.serializeNBT());
        compound.put("outputInventory", outputInventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public ResourceLocation getRecipeCategoryUID() {
        return ID;
    }

    @Override
    public SinteringFurnaceTileEntity getSelf() {
        return this;
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
        return !this.getInputInventory().getStackInSlot(0).isEmpty() && !this.getPowderInventory().getStackInSlot(0).isEmpty();
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
        powderInventory.getStackInSlot(0).shrink(1);
        inputInventory.getStackInSlot(0).shrink(1);
        if (currentRecipe.itemOut != null && !currentRecipe.itemOut.isEmpty()) {
            ItemHandlerHelper.insertItem(outputInventory, currentRecipe.itemOut.copy(), false);
        }
    }

    private boolean hasHeat() {
        return this.getWorld().getBlockState(this.getPos().down()).getBlock().isIn(WorkshopBlockTags.HOT) || this.getWorld().getBlockState(this.getPos().down(2)).getBlock().isIn(WorkshopBlockTags.HOT) ;
    }
}
