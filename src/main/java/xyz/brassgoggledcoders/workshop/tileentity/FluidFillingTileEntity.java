package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;
import xyz.brassgoggledcoders.workshop.component.machine.FixedSidedInventoryComponent;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public abstract class FluidFillingTileEntity<T extends BasicInventoryTileEntity<T>> extends BasicInventoryTileEntity<T> implements ITickableTileEntity {

    public static final int tankCapacity = 4000;//mB;
    public static final int fluidMovedPerTick = FluidAttributes.BUCKET_VOLUME / 20;
    protected final FluidTankComponent<T> tank;
    protected final InventoryComponent<T> fillingInventory;

    @SuppressWarnings("unchecked")
    public FluidFillingTileEntity(TileEntityType<T> type, int fillingSize) {
        super(type);
        this.getMachineComponent().addTank(this.tank = (FluidTankComponent<T>) new FluidTankComponent<>("tank", tankCapacity, 80, 20)
                .setTankAction(SidedFluidTankComponent.Action.BOTH)
                .setValidator(fluidStack -> fluidStack.getFluid().getFluid().getAttributes().getTemperature() < Fluids.LAVA.getAttributes().getTemperature()));
        this.getMachineComponent().addInventory(this.fillingInventory = new InventoryComponent<T>("filling", 120, 50, fillingSize)
                .setInputFilter((stack, slot) -> FluidUtil.getFluidHandler(stack).isPresent()));
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        tank.readFromNBT(compound.getCompound("capability"));
        fillingInventory.deserializeNBT(compound.getCompound("filling"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("capability", tank.writeToNBT(new CompoundNBT()));
        compound.put("filling", fillingInventory.serializeNBT());
        return super.write(compound);
    }
    
    public ItemStackHandler getFillingInventory() {
        return fillingInventory;
    }

    //Can't do it onSlotChanged like I wanted because I can't then set the stack without an infinite loop
    @Override
    public void tick() {
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            InventoryUtil.getItemStackStream(this.fillingInventory).forEach(stack -> {
                if (!this.tank.isEmpty() && !stack.isEmpty()) {
                    if (FluidUtil.tryFillContainer(stack, this.tank, fluidMovedPerTick, null, false).isSuccess()) {
                        this.fillingInventory.setStackInSlot(0, FluidUtil.tryFillContainer(stack, this.tank, fluidMovedPerTick, null, true).getResult());
                    }
                }
            });
        }
    }
}