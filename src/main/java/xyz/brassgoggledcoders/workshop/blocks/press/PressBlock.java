package xyz.brassgoggledcoders.workshop.blocks.press;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.workshop.recipes.PressRecipes;

import javax.annotation.Nullable;

public class PressBlock extends BlockTileBase {

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_0_3;

    public PressBlock() {
        super("press", Properties.from(Blocks.PISTON), PressBlock.class);

        this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL, Integer.valueOf(0)));

    }



    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PressTile();
    }

}
