package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class SeasoningBarrelBlock extends BlockTileBase<SeasoningBarrelTile> {

    public static final EnumProperty<Direction> FACING;

    public SeasoningBarrelBlock() {
        super("seasoning_barrel", Properties.from(Blocks.IRON_BLOCK), SeasoningBarrelTile.class);
        this.setDefaultState((BlockState) this.getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    public IFactory<SeasoningBarrelTile> getTileEntityFactory() {
        return SeasoningBarrelTile::new;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> state) {
        state.add(new IProperty[] { FACING });
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) this.getDefaultState().with(FACING, context.getFace());
    }

    static {
        FACING = BlockStateProperties.FACING;
    }
}
