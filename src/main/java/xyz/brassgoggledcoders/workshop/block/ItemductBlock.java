package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.tileentity.ItemductTileEntity;
import xyz.brassgoggledcoders.workshop.tileentity.SiloBarrelTileEntity;

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
                .with(UP, false));
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
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if(shouldConnect(worldIn, facingPos, facing)) {
            return stateIn.with(SixWayBlock.FACING_TO_PROPERTY_MAP.get(facing), true);
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public BlockState makeConnections(IBlockReader blockReader, BlockPos pos) {
        BlockState state = this.getDefaultState();
        for(Direction direction : Direction.values()) {
            if(shouldConnect(blockReader, pos.offset(direction), direction.getOpposite())) {
                state.with(SixWayBlock.FACING_TO_PROPERTY_MAP.get(direction), true);
            }
        }
        return state;
    }

    private boolean shouldConnect(IBlockReader blockReader, BlockPos to, @Nullable Direction facing) {
        return blockReader.getTileEntity(to) != null && blockReader.getTileEntity(to).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing).isPresent();
    }
}
