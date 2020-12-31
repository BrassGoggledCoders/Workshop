package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.tileentity.PressTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class PressBlock extends GUITileBlock<PressTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PressBlock() {
        super(Properties.from(Blocks.CHEST).notSolid(), PressTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockpos = pos.up();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (blockstate.getBlock() == WorkshopBlocks.PRESS_TOP.getBlock()) {
            worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        BlockPos blockpos = pos.up();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.getBlock() != WorkshopBlocks.PRESS_TOP.getBlock()) {
            //World newWorld = Objects.requireNonNull(world.getChunk(pos).getWorldForge()).getWorld();
            //newWorld.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) {
            BlockPos blockpos = pos.offset(Direction.UP);
            worldIn.setBlockState(blockpos, WorkshopBlocks.PRESS_TOP.getBlock().getDefaultState(), 3);
            //worldIn.notifyNeighbors(pos, Blocks.AIR);
            //state.updateNeighbors(worldIn, pos, 3);
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }
}
