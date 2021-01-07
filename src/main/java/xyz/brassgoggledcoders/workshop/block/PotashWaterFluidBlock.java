package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

public class PotashWaterFluidBlock extends FlowingFluidBlock {
    public PotashWaterFluidBlock() {
        super(WorkshopFluids.potash, Properties.from(Blocks.WATER));
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
        for(Direction direction : Direction.values()) {
            if (direction != Direction.DOWN) {
                BlockPos blockpos = pos.offset(direction);
                if (worldIn.getFluidState(blockpos).isTagged(FluidTags.LAVA)) {
                    worldIn.setBlockState(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(worldIn, pos, pos, Blocks.ANDESITE.getDefaultState()));
                    return false;
                }
            }
        }
        return true;
    }
}
