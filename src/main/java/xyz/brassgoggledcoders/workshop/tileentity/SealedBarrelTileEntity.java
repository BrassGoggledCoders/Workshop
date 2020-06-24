package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class SealedBarrelTileEntity extends BasicInventoryTileEntity<SealedBarrelTileEntity> implements ITickableTileEntity {

    public static final int tankCapacity = 4000;//mB;
    private final FluidTankComponent<SealedBarrelTileEntity> tank;
    private final InventoryComponent<SealedBarrelTileEntity> drainingInventory;
    private final InventoryComponent<SealedBarrelTileEntity> fillingInventory;

    @SuppressWarnings("unchecked")
    public SealedBarrelTileEntity() {
        super(WorkshopBlocks.SEALED_BARREL.getTileEntityType());
        int pos = 0;
        this.getMachineComponent().addTank(this.tank = (FluidTankComponent<SealedBarrelTileEntity>) new SidedFluidTankComponent<>("tank", tankCapacity, 80, 20, pos++)
                .setColor(DyeColor.MAGENTA)
                .setTankAction(SidedFluidTankComponent.Action.BOTH)
                .setValidator(fluidStack -> fluidStack.getFluid().getFluid().getAttributes().getTemperature() < Fluids.LAVA.getAttributes().getTemperature()));
        this.getMachineComponent().addInventory(this.drainingInventory = new SidedInventoryComponent<SealedBarrelTileEntity>("draining", 50, 50, 1, pos++)
                .setColor(InventoryUtil.FLUID_INPUT_COLOR)
                .setInputFilter((stack, integer) -> FluidUtil.getFluidHandler(stack).isPresent()));
        this.getMachineComponent().addInventory(this.fillingInventory = new SidedInventoryComponent<SealedBarrelTileEntity>("filling", 120, 50, 1, pos++)
                .setColor(InventoryUtil.FLUID_OUTPUT_COLOR)
                .setInputFilter((stack, slot) -> FluidUtil.getFluidHandler(stack).isPresent()));
    }

    @Override
    public void read(CompoundNBT compound) {
        tank.readFromNBT(compound.getCompound("capability"));
        drainingInventory.deserializeNBT(compound.getCompound("draining"));
        fillingInventory.deserializeNBT(compound.getCompound("filling"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("capability", tank.writeToNBT(new CompoundNBT()));
        compound.put("draining", drainingInventory.serializeNBT());
        compound.put("filling", fillingInventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public SealedBarrelTileEntity getSelf() {
        return this;
    }

    //Can't do it onSlotChanged like I wanted because I can't then set the stack without an infinite loop
    @Override
    public void tick() {
        if(this.getWorld() != null && !this.getWorld().isRemote) {
            ItemStack drainingInventoryStackInSlot = this.drainingInventory.getStackInSlot(0);
            if (!drainingInventoryStackInSlot.isEmpty()) {
                if (FluidUtil.tryEmptyContainer(drainingInventoryStackInSlot, this.tank, tankCapacity, null, false).isSuccess()) {
                    this.drainingInventory.setStackInSlot(0, FluidUtil.tryEmptyContainer(drainingInventoryStackInSlot, this.tank, tankCapacity, null, true).getResult());
                }
            }
            ItemStack fillingInventoryStackInSlot = this.fillingInventory.getStackInSlot(0);
            if (!this.tank.isEmpty() && !fillingInventoryStackInSlot.isEmpty()) {
                if (FluidUtil.tryFillContainer(fillingInventoryStackInSlot, this.tank, FluidAttributes.BUCKET_VOLUME, null, false).isSuccess()) {
                    this.fillingInventory.setStackInSlot(0, FluidUtil.tryFillContainer(fillingInventoryStackInSlot, this.tank, FluidAttributes.BUCKET_VOLUME, null, true).getResult());
                }
            }
        }
    }
}
