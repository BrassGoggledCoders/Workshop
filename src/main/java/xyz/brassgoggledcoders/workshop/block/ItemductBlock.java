package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.tileentity.ItemductTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemductBlock extends GUITileBlock<ItemductTileEntity> {

    //For visual connection to inputs
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    //For Output
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public ItemductBlock() {
        super(Properties.from(Blocks.OAK_PLANKS).notSolid(), ItemductTileEntity::new);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(FACING, Direction.DOWN)
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(UP, false)
                .with(WEST, false)
                .with(UP, false)
                .with(DOWN, false));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        handleTileEntity(worldIn, pos, ItemductTileEntity::invalidateCache);
    }

    @Override
    protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(FACING, NORTH, SOUTH, EAST, WEST, UP, DOWN));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getFace().getOpposite();
        return this.makeConnections(context.getWorld(), context.getPos()).with(FACING, direction);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return super.updatePostPlacement(stateIn.with(SixWayBlock.FACING_TO_PROPERTY_MAP.get(facing), shouldConnect(worldIn, facingPos, facing)),
                facing, facingState, worldIn, currentPos, facingPos);
    }

    public BlockState makeConnections(IBlockReader blockReader, BlockPos pos) {
        BlockState state = this.getDefaultState();
        for (Direction direction : Direction.values()) {
            if (shouldConnect(blockReader, pos.offset(direction), direction.getOpposite())) {
                state.with(SixWayBlock.FACING_TO_PROPERTY_MAP.get(direction), true);
            }
        }
        return state;
    }

    private boolean shouldConnect(IBlockReader blockReader, BlockPos to, @Nullable Direction facing) {
        if (blockReader.getTileEntity(to) != null) {
            TileEntityType<?> type = blockReader.getTileEntity(to).getType();
            return TileEntityType.HOPPER.equals(type) || WorkshopBlocks.ITEMDUCT.getTileEntityType().equals(type);
        }
        return false;
    }
}
