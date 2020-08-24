package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import javax.annotation.Nonnull;

public class FluidFunnelTileEntity extends BasicInventoryTileEntity<FluidFunnelTileEntity> {

    public static final int tankCapacity = 4000;//mB;
    protected final FluidTankComponent<FluidFunnelTileEntity> tank;
    protected int timer = 0;
    protected int interval = 20;
    private final int fluidMovedPer = FluidAttributes.BUCKET_VOLUME / 4;

    public FluidFunnelTileEntity() {
        super(WorkshopBlocks.FLUID_FUNNEL.getTileEntityType());
        int pos = 0;
        this.getMachineComponent().addTank(this.tank = (FluidTankComponent<FluidFunnelTileEntity>) new SidedFluidTankComponent<>("tank", tankCapacity, 80, 20, pos++)
                .setColor(DyeColor.MAGENTA)
                .setTankAction(SidedFluidTankComponent.Action.BOTH)
                .setValidator(fluidStack -> fluidStack.getFluid().getFluid().getAttributes().getTemperature() < Fluids.LAVA.getAttributes().getTemperature()));
    }

    @Override
    public FluidFunnelTileEntity getSelf() {
        return this;
    }

    @Override
    public void read(CompoundNBT compound) {
        tank.readFromNBT(compound.getCompound("capability"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("capability", tank.writeToNBT(new CompoundNBT()));
        return super.write(compound);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getWorld() != null && !this.getWorld().isRemote) {
            timer++;
            if(timer > interval) {
                timer = 0;
                if(!this.getWorld().isAirBlock(this.getPos().up())) {
                    FluidUtil.getFluidHandler(this.getWorld(), this.getPos().up(), Direction.DOWN).ifPresent(fluidHandler -> FluidUtil.tryFluidTransfer(tank, fluidHandler, fluidMovedPer, true));
                }
                if(!this.getWorld().isAirBlock(this.getPos().down())) {
                    FluidUtil.getFluidHandler(this.getWorld(), this.getPos().down(), Direction.UP).ifPresent(fluidHandler -> FluidUtil.tryFluidTransfer(fluidHandler, tank, fluidMovedPer, true));
                }
            }
        }
    }
}
