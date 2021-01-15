package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopCapabilities;
import xyz.brassgoggledcoders.workshop.tileentity.CollectorTileEntity;

public class CollectorBlock extends GUITileBlock<CollectorTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public CollectorBlock() {
        super(Properties.from(Blocks.STONE), CollectorTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        buildCache(state, worldIn, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(fromPos.equals(pos.offset(state.get(FACING)))) {
            buildCache(state, worldIn, pos);
        }
    }

    private void buildCache(BlockState state, World worldIn, BlockPos pos) {
        Direction facing = state.get(FACING);
        BlockPos other = pos.offset(facing);
        if (worldIn.getTileEntity(pos) instanceof CollectorTileEntity) {
            this.handleTileEntity(worldIn, pos, tile -> {
                TileEntity tileEntity = worldIn.getTileEntity(other);
                if(tileEntity != null) {
                    tile.type = tileEntity.getType();
                    tile.capability = tile.getCapability(WorkshopCapabilities.COLLECTOR_TARGET, facing.getOpposite());
                    tile.capability.addListener((t) -> tile.invalidateCache());
                } else {
                    tile.type = null;
                    tile.invalidateCache();
                }
            });
        }
    }
}
