package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.workshop.component.machine.FixedSidedInventoryComponent;
import xyz.brassgoggledcoders.workshop.component.machine.FixedSidedTankComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.MortarRecipe;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class MortarTileEntity extends BasicMachineTileEntity<MortarTileEntity, MortarRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.MORTAR_SERIALIZER.get().getRecipeType().toString());
    private final InventoryComponent<MortarTileEntity> input;
    private final FluidTankComponent<MortarTileEntity> fluidInput;
    private final InventoryComponent<MortarTileEntity> output;

    public static final int tankSize = 1000; // mB
    public static final int inputSize = 6;

    public MortarTileEntity() {
        super(WorkshopBlocks.MORTAR.getTileEntityType(), new ProgressBarComponent<MortarTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.ARROW_RIGHT));
        this.getMachineComponent().addInventory(this.input = new FixedSidedInventoryComponent<MortarTileEntity>(InventoryUtil.ITEM_INPUT, 10, 25, inputSize, FixedSidedInventoryComponent.NOT_BOTTOM)
                .setRange(2, 3)
                .setOnSlotChanged((stack, integer) -> this.getMachineComponent().forceRecipeRecheck()));
        this.getMachineComponent().addTank(this.fluidInput = new FixedSidedTankComponent<MortarTileEntity>(
                InventoryUtil.FLUID_INPUT, tankSize, 50, 20, FixedSidedInventoryComponent.NOT_BOTTOM)
                .setTankAction(SidedFluidTankComponent.Action.FILL)
                .setOnContentChange(this.getMachineComponent()::forceRecipeRecheck));
        this.getMachineComponent().addInventory(this.output = new FixedSidedInventoryComponent<MortarTileEntity>(InventoryUtil.ITEM_OUTPUT, 102, 44, 1, FacingUtil.Sideness.BOTTOM)
                .setInputFilter((stack, integer) -> false));
        this.getMachineComponent().getPrimaryBar().setCanIncrease(tileEntity -> false);
        this.getMachineComponent().getPrimaryBar().setCanReset(tileEntity -> false);
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        input.deserializeNBT(compound.getCompound("input"));
        fluidInput.readFromNBT(compound.getCompound("inputFluidTank"));
        output.deserializeNBT(compound.getCompound("output"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("input", input.serializeNBT());
        compound.put("inputFluidTank", fluidInput.writeToNBT(new CompoundNBT()));
        compound.put("output", output.serializeNBT());
        return super.write(compound);
    }

    public ItemStackHandler getInputInventory() {
        return this.input;
    }

    public ItemStackHandler getOutputInventory() {
        return this.output;
    }

    @Override
    public ResourceLocation getRecipeCategoryUID() {
        return ID;
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
        return recipe.getType() == WorkshopRecipes.MORTAR_SERIALIZER.get().getRecipeType() && recipe instanceof MortarRecipe;
    }

    @Override
    public MortarRecipe castRecipe(IRecipe<?> iRecipe) {
        return (MortarRecipe) iRecipe;
    }

    @Override
    public boolean matchesInputs(MortarRecipe currentRecipe) {
        return ItemHandlerHelper.insertItemStacked(this.output, currentRecipe.output, true).isEmpty() && currentRecipe.matches(input, fluidInput);
    }

    @Override
    public void handleComplete(MortarRecipe currentRecipe) {
        fluidInput.drainForced(currentRecipe.fluidInput, IFluidHandler.FluidAction.EXECUTE);
        for (int i = 0; i < input.getSlots(); i++) {
            input.getStackInSlot(i).shrink(1);
        }
        if (!currentRecipe.output.isEmpty()) {
            ItemHandlerHelper.insertItem(output, currentRecipe.output.copy(), false);
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
