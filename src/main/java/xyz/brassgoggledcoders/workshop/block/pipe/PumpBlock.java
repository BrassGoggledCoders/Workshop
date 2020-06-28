package xyz.brassgoggledcoders.workshop.block.pipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import xyz.brassgoggledcoders.workshop.tileentity.pipe.PumpTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PumpBlock extends Block {
    public static final EnumProperty<Direction> OUTPUT = BlockStateProperties.FACING;

    public PumpBlock() {
        this(Properties.create(Material.IRON)
                .harvestTool(ToolType.PICKAXE)
        );
    }

    public PumpBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(OUTPUT, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(OUTPUT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return this.getDefaultState().with(OUTPUT, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState blockState, IBlockReader blockReader) {
        return new PumpTileEntity();
    }
}
