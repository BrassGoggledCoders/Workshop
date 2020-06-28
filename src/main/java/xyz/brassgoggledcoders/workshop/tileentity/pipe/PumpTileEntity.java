package xyz.brassgoggledcoders.workshop.tileentity.pipe;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.pipe.PumpBlock;
import xyz.brassgoggledcoders.workshop.capabilities.pipe.PipeNetworkCapability;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import java.util.Map;

public class PumpTileEntity extends TileEntity implements ITickableTileEntity {
    private LazyOptional<PipeNetworkCapability> pipeNetwork;

    public PumpTileEntity() {
        super(WorkshopBlocks.PUMP.getTileEntityType());
    }

    @Override
    public void tick() {
        if (pipeNetwork == null) {
            if (this.getWorld() != null) {
                pipeNetwork = this.getWorld().getCapability(PipeNetworkCapability.CAP);
            }
        } else {
            pipeNetwork.ifPresent(this::handlePiping);
        }
    }

    private void handlePiping(PipeNetworkCapability pipeNetworkCapability) {
        Map<BlockPos, Direction> outputs = pipeNetworkCapability.getPotentialOutputs(this.pos, this.getBlockState()
                .get(PumpBlock.OUTPUT));
        Workshop.LOGGER.info(outputs.size());
    }
}
