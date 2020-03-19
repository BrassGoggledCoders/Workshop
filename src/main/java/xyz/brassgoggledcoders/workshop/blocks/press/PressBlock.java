package xyz.brassgoggledcoders.workshop.blocks.press;

import javax.annotation.Nonnull;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockRotation;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class PressBlock extends BlockRotation<PressTile> {

    public PressBlock() {
        super("press", Properties.from(Blocks.IRON_BLOCK), PressTile.class);
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public IFactory<PressTile> getTileEntityFactory() {
        return PressTile::new;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D);
    }
}
