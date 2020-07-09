package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PressTopBlock extends Block {
    public PressTopBlock() {
        super(Block.Properties.from(WorkshopBlocks.PRESS.getBlock()));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockpos = pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (blockstate.getBlock() == WorkshopBlocks.PRESS.getBlock()) {
            worldIn.destroyBlock(blockpos, true);
            //worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        BlockPos blockpos = pos.down();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.getBlock() != WorkshopBlocks.PRESS.getBlock()) {
            World newWorld = Objects.requireNonNull(world.getChunk(pos).getWorldForge()).getWorld();
            newWorld.setBlockState(pos, Blocks.AIR.getDefaultState());
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
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
        handleTileEntity(worldIn, pos, tile -> result.set(tile.onActivated(player, handIn, hit)));
        return result.get();
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, Consumer<? super GUITile> tileEntityConsumer) {
        Optional.ofNullable(world.getTileEntity(pos.down()))
                .filter(tileEntity -> tileEntity instanceof GUITile)
                .map(GUITile.class::cast)
                .ifPresent(tileEntityConsumer);
    }
}
