package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class SeasoningBarrelBlock extends BlockTileBase {

    public static final EnumProperty<Direction> FACING;

    public SeasoningBarrelBlock() {
        super("seasoning_barrel", Properties.from(Blocks.IRON_BLOCK), SeasoningBarrelTile.class);
        this.setDefaultState((BlockState)this.getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    public IFactory getTileEntityFactory() {
        return SeasoningBarrelTile::new;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> state) {
        state.add(new IProperty[]{FACING});
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState)this.getDefaultState().with(FACING, context.getFace());
    }

    static {
        FACING = BlockStateProperties.FACING;
    }
}
