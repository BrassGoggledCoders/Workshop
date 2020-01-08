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

public class SeasoningBarrelBlock extends BlockTileBase {

    public static final EnumProperty<Direction.Axis> AXIS;

    public SeasoningBarrelBlock() {
        super("seasoning_barrel", Properties.from(Blocks.IRON_BLOCK), SeasoningBarrelTile.class);
        this.setDefaultState((BlockState)this.getDefaultState().with(AXIS, Direction.Axis.Y));
    }

    @Override
    public IFactory getTileEntityFactory() {
        return SeasoningBarrelTile::new;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch(rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch((Direction.Axis)state.get(AXIS)) {
                    case X:
                        return (BlockState)state.with(AXIS, Direction.Axis.Z);
                    case Z:
                        return (BlockState)state.with(AXIS, Direction.Axis.X);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> state) {
        state.add(new IProperty[]{AXIS});
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState)this.getDefaultState().with(AXIS, context.getFace().getAxis());
    }

    static {
        AXIS = BlockStateProperties.AXIS;
    }
}
