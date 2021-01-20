package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import java.util.function.Supplier;

public class LavaInteractableFluidBlock extends FlowingFluidBlock {

    private final Supplier<BlockState> created;

    public LavaInteractableFluidBlock(Supplier<? extends FlowingFluid> supplier, AbstractBlock.Properties properties, Supplier<BlockState> created) {
        super(supplier, properties);
        this.created = created;
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (this.reactWithNeighbors(worldIn, pos)) {
            worldIn.getPendingFluidTicks().scheduleTick(pos, state.getFluidState().getFluid(), this.getFluid().getTickRate(worldIn));
        }
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (this.reactWithNeighbors(worldIn, pos)) {
            worldIn.getPendingFluidTicks().scheduleTick(pos, state.getFluidState().getFluid(), this.getFluid().getTickRate(worldIn));
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    private boolean reactWithNeighbors(World worldIn, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (direction != Direction.DOWN) {
                BlockPos blockpos = pos.offset(direction);
                if (worldIn.getFluidState(blockpos).isTagged(FluidTags.LAVA)) {
                    worldIn.setBlockState(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(worldIn, pos, pos, created.get()));
                    return false;
                }
            }
        }
        return true;
    }
}
