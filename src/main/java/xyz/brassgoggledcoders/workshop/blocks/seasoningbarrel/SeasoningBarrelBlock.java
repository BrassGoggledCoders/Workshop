package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class SeasoningBarrelBlock extends BasicTileBlock<SeasoningBarrelTile> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public SeasoningBarrelBlock() {
        super(Properties.from(Blocks.OAK_PLANKS).notSolid(), SeasoningBarrelTile.class);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    public IFactory<SeasoningBarrelTile> getTileEntityFactory() {
        return SeasoningBarrelTile::new;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace());
    }
}
