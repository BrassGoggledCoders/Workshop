package xyz.brassgoggledcoders.workshop.blocks.press;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import xyz.brassgoggledcoders.workshop.recipes.PressRecipes;

import javax.annotation.Nullable;

public class PressBlock extends BlockTileBase {

    public PressBlock() {
        super("press", Properties.from(Blocks.IRON_BLOCK), PressBlock.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return PressTile::new;
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
