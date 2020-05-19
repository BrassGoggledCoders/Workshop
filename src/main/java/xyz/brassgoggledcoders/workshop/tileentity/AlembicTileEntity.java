package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.datagen.tags.WorkshopItemTagsProvider;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

import javax.annotation.Nonnull;

public class AlembicTileEntity extends BasicMachineTileEntity<AlembicTileEntity, AlembicRecipe> {

    private final InventoryComponent<AlembicTileEntity> input;
    private final InventoryComponent<AlembicTileEntity> container;
    private final InventoryComponent<AlembicTileEntity> residue;
    private final InventoryComponent<AlembicTileEntity> output;
    private InventoryComponent<AlembicTileEntity> coldItem;
    private final ProgressBarComponent<AlembicTileEntity> meltTime;

    public static final int inputSize = 3;
    public static final int residueSize = 4;

    public AlembicTileEntity() {
        super(WorkshopBlocks.ALEMBIC.getTileEntityType(), new ProgressBarComponent<AlembicTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.input = new SidedInventoryComponent<AlembicTileEntity>(InventoryUtil.ITEM_INPUT, 34, 25, inputSize, pos++)
                .setColor(InventoryUtil.ITEM_INPUT_COLOR)
                .setRange(1, 3));
        this.getMachineComponent().addInventory(this.container = new SidedInventoryComponent<AlembicTileEntity>("container", 56, 43, 1, pos++)
                .setColor(DyeColor.WHITE)
                .setInputFilter((stack, integer) -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()));
        this.getMachineComponent().addInventory(this.output = new SidedInventoryComponent<AlembicTileEntity>(InventoryUtil.ITEM_OUTPUT, 102, 44, 1, pos++)
                .setColor(InventoryUtil.ITEM_OUTPUT_COLOR)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().addInventory(this.residue = new SidedInventoryComponent<AlembicTileEntity>(
                "residue", 125, 25, residueSize, pos++)
                .setColor(DyeColor.YELLOW)
                .setRange(1, 3)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().addInventory(this.coldItem = new SidedInventoryComponent<AlembicTileEntity>("coldItem", 79, 20, 1, pos++)
                .setColor(DyeColor.LIGHT_BLUE)
                .setInputFilter((stack, integer) -> stack.getItem().isIn(WorkshopItemTagsProvider.COLD))
                .setOnSlotChanged((stack, slot) -> {
                    if(this.coldItem.getStackInSlot(0).isEmpty()) {
                        this.getMachineComponent().getPrimaryBar().setProgressIncrease(1);
                    }
                    else {
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
        container.deserializeNBT(compound.getCompound("container"));
        residue.deserializeNBT(compound.getCompound("residue"));
        output.deserializeNBT(compound.getCompound("output"));
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
        compound.put("container", container.serializeNBT());
        compound.put("residue", residue.serializeNBT());
        compound.put("output", output.serializeNBT());
        compound.put("coldItem", coldItem.serializeNBT());
        compound.put("meltTime", meltTime.serializeNBT());
        return super.write(compound);
    }

    @Override
    public AlembicTileEntity getSelf() {
        return this;
    }

    @Override
    public boolean hasInputs() {
        if(!this.container.getStackInSlot(0).isEmpty()) {
            LazyOptional<IFluidHandlerItem> optional = this.container.getStackInSlot(0).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
            //Should never not be present because of the slot filter but better safe than sorry
            if(optional.isPresent()) {
                //Check we have input items, and that the fluid container doesn't have any fluid
                return InventoryUtil.anySlotsHaveItems(input) && optional.orElseThrow(NullPointerException::new).getFluidInTank(0).isEmpty();
            }
        }
        return false;
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
        //TODO Handle stackable fluid containers
        return this.output.getStackInSlot(0).isEmpty() && currentRecipe.matches(input);
    }

    @Override
    public void handleComplete(AlembicRecipe currentRecipe) {
        for (int i = 0; i < input.getSlots(); i++) {
            input.getStackInSlot(i).shrink(1);
        }
        if (currentRecipe.output != null && !currentRecipe.output.isEmpty()) {
            ItemStack stack = this.container.getStackInSlot(0);
            if (!stack.isEmpty()) {
                //Work on a copy
                ItemStack outputStack = stack.copy();
                //Should always have the cap because no items that don't have it should be in the slot...but check anyway
                outputStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(cap -> {
                    //If there's space in the container
                    if (cap.fill(currentRecipe.output, IFluidHandler.FluidAction.SIMULATE) == currentRecipe.output.getAmount()) {
                        //Fill it
                        cap.fill(currentRecipe.output, IFluidHandler.FluidAction.EXECUTE);
                        //Insert it to output
                        ItemHandlerHelper.insertItem(output, cap.getContainer(), false);
                        //Now remove it from input
                        stack.shrink(1);
                    }
                });
            }
            if (currentRecipe.residue != null && world instanceof ServerWorld) {
                for (RangedItemStack rStack : currentRecipe.residue) {
                    ItemHandlerHelper.insertItem(this.residue, RangedItemStack.getOutput(world.rand, rStack), false);
                }
            }
        }
    }
}
