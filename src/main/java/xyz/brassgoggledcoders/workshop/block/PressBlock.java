package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
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
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.tileentity.PressTileEntity;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class PressBlock extends GUITileBlock<PressTileEntity> {

    public static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(3, 3, 13, 13, 15, 14),
            Block.makeCuboidShape(3, 3, 2, 13, 15, 3),
            Block.makeCuboidShape(13, 3, 3, 14, 15, 13),
            Block.makeCuboidShape(2, 3, 3, 3, 15, 13),
            Block.makeCuboidShape(3, 19, 3, 13, 21, 13),
            Block.makeCuboidShape(3, 21, 3, 13, 28, 13),
            Block.makeCuboidShape(13, 4, 0, 16, 28, 3),
            Block.makeCuboidShape(13, 4, 13, 16, 28, 16),
            Block.makeCuboidShape(0, 4, 0, 3, 28, 3),
            Block.makeCuboidShape(0, 4, 13, 3, 28, 16),
            Block.makeCuboidShape(0, 28, 0, 16, 32, 16),
            Block.makeCuboidShape(0, 0, 0, 16, 4, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PressBlock() {
        super(Properties.from(Blocks.CHEST).notSolid(), PressTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote && !fromPos.equals(pos.up())) {
            if(worldIn.isBlockPowered(pos)) {
                this.handleTileEntity(worldIn, pos, PressTileEntity::trigger);
            }
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.isIn(state.getBlock())) {
            if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
                if(worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up())) {
                    this.handleTileEntity(worldIn, pos, PressTileEntity::trigger);
                }
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!worldIn.isRemote) {
            BlockPos blockpos = pos.up();
            BlockState blockstate = worldIn.getBlockState(blockpos);
            if (blockstate.getBlock() == WorkshopBlocks.PRESS_TOP.getBlock()) {
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
                worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) {
            BlockPos blockpos = pos.up();
            worldIn.setBlockState(blockpos, WorkshopBlocks.PRESS_TOP.getBlock().getDefaultState(), 3);
            if(worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up())) {
                this.handleTileEntity(worldIn, pos, PressTileEntity::trigger);
            }
        }
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

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
