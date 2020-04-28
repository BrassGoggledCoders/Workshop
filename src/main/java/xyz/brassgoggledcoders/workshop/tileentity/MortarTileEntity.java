package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.MortarRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class MortarTileEntity extends BasicMachineTileEntity<MortarTileEntity, MortarRecipe> {

    private final InventoryComponent<MortarTileEntity> input;
    private final FluidTankComponent<MortarTileEntity> fluidInput;
    private final InventoryComponent<MortarTileEntity> output;

    public static final int tankSize = 1000; // mB
    public static final int inputSize = 6;

    public MortarTileEntity() {
        super(WorkshopBlocks.MORTAR.getTileEntityType(), new ProgressBarComponent<MortarTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        int pos = 0;
        this.getMachineComponent().addInventory(this.input = new SidedInventoryComponent<MortarTileEntity>("input", 10, 25, inputSize, pos++)
                .setColor(DyeColor.RED)
                .setRange(2, 3)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.fluidInput = new SidedFluidTankComponent<MortarTileEntity>(
                "inputFluidTank", tankSize, 50, 20, pos++)
                .setColor(DyeColor.BROWN)
                .setTankAction(SidedFluidTankComponent.Action.FILL)
                .setOnContentChange(this.getMachineComponent()::forceRecipeRecheck));
        this.getMachineComponent().addInventory(this.output = new SidedInventoryComponent<MortarTileEntity>("output", 102, 44, 1, pos++)
                .setColor(DyeColor.BLACK)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().getPrimaryBar().setCanIncrease(tileEntity -> false);
        this.getMachineComponent().getPrimaryBar().setCanReset(tileEntity -> false);
    }

    @Override
    public void read(CompoundNBT compound) {
        input.deserializeNBT(compound.getCompound("input"));
        fluidInput.readFromNBT(compound.getCompound("inputFluidTank"));
        output.deserializeNBT(compound.getCompound("output"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("input", input.serializeNBT());
        compound.put("inputFluidTank", fluidInput.writeToNBT(new CompoundNBT()));
        compound.put("output", output.serializeNBT());
        return super.write(compound);
    }

    @Override
    public MortarTileEntity getSelf() {
        return this;
    }

    @Override
    public boolean hasInputs() {
        return InventoryUtil.anySlotsHaveItems(input) || !fluidInput.isEmpty();
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.MORTAR && recipe instanceof MortarRecipe;
    }

    @Override
    public MortarRecipe castRecipe(IRecipe<?> iRecipe) {
        return (MortarRecipe) iRecipe;
    }

    @Override
    public boolean matchesInputs(MortarRecipe currentRecipe) {
        return currentRecipe.matches(input, fluidInput);
    }

    @Override
    public void handleComplete(MortarRecipe currentRecipe) {
        fluidInput.drainForced(currentRecipe.fluidInput, IFluidHandler.FluidAction.EXECUTE);
        for (int i = 0; i < input.getSlots(); i++) {
            input.getStackInSlot(i).shrink(1);
        }
        ItemStack itemOut = currentRecipe.output;
        if (itemOut != null) {
            output.insertItem(0, itemOut, false);
        }
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (this.getWorld() != null && !this.getWorld().isRemote()) {
            if (!player.isCrouching()) {
                //Open GUI
                return super.onActivated(player, hand, hit);
            } else {
                this.getMachineComponent().getPrimaryBar().setProgress(this.getMachineComponent().getPrimaryBar().getProgress() + 1);
                return ActionResultType.SUCCESS;
            }
        }
        return super.onActivated(player, hand, hit);
    }
}
