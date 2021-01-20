package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.tileentity.GUITile;
import xyz.brassgoggledcoders.workshop.tileentity.PressTileEntity;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class PressTopBlock extends Block {
    public PressTopBlock() {
        super(Block.Properties.from(WorkshopBlocks.PRESS.getBlock()));
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockpos = pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (blockstate.getBlock() == WorkshopBlocks.PRESS.getBlock()) {
            worldIn.destroyBlock(blockpos, true);
            worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
        }
        super.onBlockHarvested(worldIn, pos, state, player);
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
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!worldIn.isRemote) {
            if (worldIn.getBlockState(pos.down()).getBlock() != WorkshopBlocks.PRESS.getBlock()) {
                worldIn.removeBlock(pos, false);
            }
            if(!fromPos.equals(pos.down()) && worldIn.isBlockPowered(pos)) {
                this.handleTileEntity(worldIn, pos, PressTileEntity::trigger);
            }
        }
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0.0D, -16.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
        handleTileEntity(worldIn, pos, tile -> result.set(tile.onActivated(player, handIn, hit)));
        return result.get();
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, Consumer<PressTileEntity> tileEntityConsumer) {
        Optional.ofNullable(world.getTileEntity(pos.down()))
                .filter(tileEntity -> tileEntity instanceof PressTileEntity)
                .map(PressTileEntity.class::cast)
                .ifPresent(tileEntityConsumer);
    }
}
