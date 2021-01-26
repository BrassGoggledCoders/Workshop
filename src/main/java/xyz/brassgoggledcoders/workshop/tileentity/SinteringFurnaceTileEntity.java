package xyz.brassgoggledcoders.workshop.tileentity;


import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.block.SinteringFurnaceBlock;
import xyz.brassgoggledcoders.workshop.component.machine.FixedSidedInventoryComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;
import xyz.brassgoggledcoders.workshop.tag.WorkshopBlockTags;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class SinteringFurnaceTileEntity extends BasicMachineTileEntity<SinteringFurnaceTileEntity, SinteringFurnaceRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.SINTERING_FURNACE_SERIALIZER.get().getRecipeType().toString());
    private final InventoryComponent<SinteringFurnaceTileEntity> powderInventory;
    private final InventoryComponent<SinteringFurnaceTileEntity> inputInventory;
    private final InventoryComponent<SinteringFurnaceTileEntity> outputInventory;
    protected int timer = 0;
    protected final int interval = 20;

    public SinteringFurnaceTileEntity() {
        super(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(),
                new ProgressBarComponent<SinteringFurnaceTileEntity>(76, 42, 100)
                        .setBarDirection(ProgressBarComponent.BarDirection.ARROW_RIGHT));
        //TODO Prevent insertion by hand
        this.getMachineComponent().addInventory(this.powderInventory = new FixedSidedInventoryComponent<SinteringFurnaceTileEntity>("powderInventory", 70, 19, 1, FacingUtil.Sideness.TOP)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.inputInventory = new FixedSidedInventoryComponent<SinteringFurnaceTileEntity>(InventoryUtil.ITEM_INPUT, 34, 42, 1,
                FacingUtil.Sideness.BACK, FacingUtil.Sideness.FRONT, FacingUtil.Sideness.LEFT, FacingUtil.Sideness.RIGHT)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addInventory(this.outputInventory = new FixedSidedInventoryComponent<>(InventoryUtil.ITEM_OUTPUT, 120, 42, 1, FacingUtil.Sideness.BOTTOM));
        this.getMachineComponent().getPrimaryBar().setCanIncrease((tile) -> hasHeat() && tile.hasInputs() && tile.getMachineComponent().getCurrentRecipe() != null);
    }

    //FIXME Efficiency. Cache state checks
    @Override
    public void tick() {
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            timer++;
            if (timer > interval) {
                timer = 0;
                if (this.hasWorld() && this.getBlockState().get(SinteringFurnaceBlock.LIT) != this.hasHeat()) {
                    this.getWorld().setBlockState(this.pos, this.getBlockState().with(SinteringFurnaceBlock.LIT, hasHeat()), 3);
                    this.markDirty();
                    this.markComponentDirty();
                }
            }
        }
        super.tick();
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
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

    public InventoryComponent<SinteringFurnaceTileEntity> getOutputInventory() {
        return outputInventory;
    }

    public InventoryComponent<SinteringFurnaceTileEntity> getPowderInventory() {
        return powderInventory;
    }

    public InventoryComponent<SinteringFurnaceTileEntity> getInputInventory() {
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
        return this.getWorld().getBlockState(this.getPos().down()).getBlock().isIn(WorkshopBlockTags.HOT) || this.getWorld().getBlockState(this.getPos().down(2)).getBlock().isIn(WorkshopBlockTags.HOT);
    }
}
