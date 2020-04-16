package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class AlembicTileEntity extends BasicMachineTileEntity<AlembicTileEntity, AlembicRecipe> {

    private InventoryComponent<AlembicTileEntity> input;
    private InventoryComponent<AlembicTileEntity> container;
    private InventoryComponent<AlembicTileEntity> residue;
    private InventoryComponent<AlembicTileEntity> output;
    //private InventoryComponent<AlembicTileEntity> coldItem;
    //private HeatBarComponent<AlembicTileEntity> alembicTemp;

    //private int coldTime;
    //private int temp;
    //private int maxTemp = 5000;

    public static final int inputSize = 3;
    public static final int residueSize = 4;

    public AlembicTileEntity() {
        super(WorkshopBlocks.ALEMBIC.getTileEntityType(), new ProgressBarComponent<AlembicTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.input = new SidedInventoryComponent<AlembicTileEntity>("input", 34, 25, inputSize, pos++)
                .setColor(DyeColor.RED)
                .setRange(1, 3));
        this.getMachineComponent().addInventory(this.container = new SidedInventoryComponent<AlembicTileEntity>("container", 56, 43, 1, pos++)
                .setColor(DyeColor.WHITE)
                .setInputFilter((stack, integer) -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()));
        this.getMachineComponent().addInventory(this.output = new SidedInventoryComponent<AlembicTileEntity>("output", 102, 44, 1, pos++)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().addInventory(this.residue = new SidedInventoryComponent<AlembicTileEntity>(
                "residue", 125, 25, residueSize, pos++)
                .setColor(DyeColor.YELLOW)
                .setRange(1, 3)
                .setInputFilter((stack, integer) -> false));
        //this.getMachineComponent().addInventory(this.coldItem = new SidedInventoryComponent<AlembicTileEntity>("coldItem", 79, 20, 1, pos++)
        //        .setColor(DyeColor.LIGHT_BLUE)
        //        .setInputFilter((stack, integer) -> stack.getItem().isIn(COLD)));
        //this.alembicTemp = new HeatBarComponent<AlembicTileEntity>(100, 20, temp, getMaxTemp()).setColor(DyeColor.LIGHT_BLUE);
    }

    @Override
    public void read(CompoundNBT compound) {
        input.deserializeNBT(compound.getCompound("input"));
        container.deserializeNBT(compound.getCompound("container"));
        residue.deserializeNBT(compound.getCompound("residue"));
        output.deserializeNBT(compound.getCompound("output"));
        //coldItem.deserializeNBT(compound.getCompound("coldItem"));
        //alembicTemp.deserializeNBT(compound.getCompound("temp"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("input", input.serializeNBT());
        compound.put("container", container.serializeNBT());
        compound.put("residue", container.serializeNBT());
        compound.put("output", container.serializeNBT());
        //compound.put("coldItem", container.serializeNBT());
        //compound.put("temp", container.serializeNBT());
        return super.write(compound);
    }

    @Override
    public AlembicTileEntity getSelf() {
        return this;
    }

    @Override
    public boolean hasInputs() {
        return InventoryUtil.anySlotsHaveItems(input) && !this.container.getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.ALEMBIC && recipe instanceof AlembicRecipe;
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
        return currentRecipe.matches(input);
    }

    @Override
    public void handleComplete(AlembicRecipe currentRecipe) {
        for (int i = 0; i < input.getSlots(); i++) {
            input.getStackInSlot(i).shrink(1);
        }
        if (currentRecipe.output != null && !currentRecipe.output.isEmpty()) {
            ItemStack stack = this.container.getStackInSlot(0);
            if(!stack.isEmpty()) {
                //Work on a copy
                ItemStack outputStack = stack.copy();
                //Should always have the cap because no items that don't have it should be in the slot...but check anyway
                outputStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent( cap -> {
                    //If there's space in the container
                    if(cap.fill(currentRecipe.output, IFluidHandler.FluidAction.SIMULATE) == currentRecipe.output.getAmount()) {
                        //Fill it
                        cap.fill(currentRecipe.output, IFluidHandler.FluidAction.EXECUTE);
                        //Insert it to output
                        ItemHandlerHelper.insertItem(output, cap.getContainer(), false);
                        //And remove it from input
                        stack.shrink(1);
                    }
                });
            }
            if(currentRecipe.residue != null) {
                for (ItemStack residueIn : currentRecipe.residue) {
                    ItemHandlerHelper.insertItemStacked(residue, residueIn, false);
                }
            }
        }
    }
}
