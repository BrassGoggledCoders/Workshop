package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.datagen.tags.WorkshopItemTagsProvider;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AlembicTileEntity extends BasicMachineTileEntity<AlembicTileEntity, AlembicRecipe> {

    private final InventoryComponent<AlembicTileEntity> input;
    private final FluidTankComponent<AlembicTileEntity> output;
    private final InventoryComponent<AlembicTileEntity> residue;
    private InventoryComponent<AlembicTileEntity> coldItem;
    private final ProgressBarComponent<AlembicTileEntity> meltTime;

    public static final int inputSize = 3;
    public static final int residueSize = 4;

    public AlembicTileEntity() {
        super(WorkshopBlocks.ALEMBIC.getTileEntityType(), new ProgressBarComponent<AlembicTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.input = new SidedInventoryComponent<AlembicTileEntity>(InventoryUtil.ITEM_INPUT, 34, 25, inputSize, pos++)
                .setColor(InventoryUtil.ITEM_INPUT_COLOR)
                .setRange(1, 3)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.output = new SidedFluidTankComponent<AlembicTileEntity>(InventoryUtil.FLUID_OUTPUT, 4000, 100, 20, pos++).
                setColor(InventoryUtil.FLUID_OUTPUT_COLOR).
                setTankAction(SidedFluidTankComponent.Action.DRAIN));
        this.getMachineComponent().addInventory(this.residue = new SidedInventoryComponent<AlembicTileEntity>(
                "residue", 125, 25, residueSize, pos++)
                .setColor(DyeColor.YELLOW)
                .setRange(1, residueSize)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().addInventory(this.coldItem = new SidedInventoryComponent<AlembicTileEntity>("coldItem", 79, 20, 1, pos++)
                .setColor(DyeColor.LIGHT_BLUE)
                .setInputFilter((stack, integer) -> stack.getItem().isIn(WorkshopItemTagsProvider.COLD))
                .setOnSlotChanged((stack, slot) -> {
                    if (this.coldItem.getStackInSlot(0).isEmpty()) {
                        this.getMachineComponent().getPrimaryBar().setProgressIncrease(1);
                    } else {
                        this.getMachineComponent().getPrimaryBar().setProgressIncrease(3);
                    }
                }));
        this.getMachineComponent().addProgressBar(this.meltTime = new ProgressBarComponent<AlembicTileEntity>(145, 20, 20 * 60 * 2/*2min*/)
                .setBarDirection(ProgressBarComponent.BarDirection.VERTICAL_UP)
                .setCanIncrease((tileEntity) -> !tileEntity.coldItem.getStackInSlot(0).isEmpty())
                .setOnFinishWork(() -> this.coldItem.getStackInSlot(0).shrink(1)));
    }

    @Override
    public void read(CompoundNBT compound) {
        input.deserializeNBT(compound.getCompound("input"));
        residue.deserializeNBT(compound.getCompound("residue"));
        output.readFromNBT(compound.getCompound("output"));
        coldItem.deserializeNBT(compound.getCompound("coldItem"));
        //Ensure we check for cold items on load
        coldItem.getOnSlotChanged().accept(coldItem.getStackInSlot(0), 0);
        meltTime.deserializeNBT(compound.getCompound("meltTime"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("input", input.serializeNBT());
        compound.put("residue", residue.serializeNBT());
        compound.put("output", output.writeToNBT(new CompoundNBT()));
        compound.put("coldItem", coldItem.serializeNBT());
        compound.put("meltTime", meltTime.serializeNBT());
        return super.write(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT updateTag = new CompoundNBT();
        updateTag.put("output", output.writeToNBT(new CompoundNBT()));
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        output.readFromNBT(tag.getCompound("output"));
    }

    @Override
    public AlembicTileEntity getSelf() {
        return this;
    }

    @Override
    public boolean hasInputs() {
        return InventoryUtil.anySlotsHaveItems(input);
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.ALEMBIC_SERIALIZER.get().getRecipeType() && recipe instanceof AlembicRecipe;
    }

    @Override
    public AlembicRecipe castRecipe(IRecipe<?> iRecipe) {
        return (AlembicRecipe) iRecipe;
    }

    @Override
    public int getProcessingTime(AlembicRecipe currentRecipe) {
        return currentRecipe.getProcessingTime();
    }

    @Override
    public boolean matchesInputs(AlembicRecipe currentRecipe) {
        return this.output.fill(currentRecipe.output, IFluidHandler.FluidAction.SIMULATE) == 0 && currentRecipe.matches(input);
    }

    @Override
    public void handleComplete(AlembicRecipe currentRecipe) {
        //TODO
        for (int i = 0; i < input.getSlots(); i++) {
            input.getStackInSlot(i).shrink(1);
        }
        if (currentRecipe.output != null && !currentRecipe.output.isEmpty()) {
            output.fillForced(currentRecipe.output.copy(), IFluidHandler.FluidAction.EXECUTE);
            if (currentRecipe.residue != null && world instanceof ServerWorld) {
                for (RangedItemStack rStack : currentRecipe.residue) {
                    ItemHandlerHelper.insertItem(this.residue, RangedItemStack.getOutput(world.rand, rStack), false);
                }
            }
        }
    }
}
