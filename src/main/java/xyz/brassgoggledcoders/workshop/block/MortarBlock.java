package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.workshop.tileentity.MortarTileEntity;

import javax.annotation.Nonnull;

public class MortarBlock extends GUITileBlock<MortarTileEntity> {

    public MortarBlock() {
        super(Properties.from(Blocks.GRANITE).notSolid(), MortarTileEntity::new);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 3.0D, 12.0D);
    }
}
