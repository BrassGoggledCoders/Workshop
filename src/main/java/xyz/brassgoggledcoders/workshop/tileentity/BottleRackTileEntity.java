package xyz.brassgoggledcoders.workshop.tileentity;


import net.minecraftforge.fluids.FluidUtil;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import java.util.concurrent.atomic.AtomicBoolean;

public class BottleRackTileEntity extends FluidFillingTileEntity<BottleRackTileEntity> {

    public BottleRackTileEntity() {
        super(WorkshopBlocks.BOTTLE_RACK.getTileEntityType(), 4);
        this.fillingInventory.setOutputFilter(((itemStack, integer) -> {
            AtomicBoolean flag = new AtomicBoolean(false);
            FluidUtil.getFluidHandler(itemStack).ifPresent(fluidHandler -> flag.set(fluidHandler.getFluidInTank(0).getAmount() == fluidHandler.getTankCapacity(0)));
            return flag.get();
        }));
    }

    @Override
    public BottleRackTileEntity getSelf() {
        return this;
    }
}
