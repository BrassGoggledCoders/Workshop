package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class SpinningWheelBlock extends BlockTileBase {

    public SpinningWheelBlock() {
        super("spinning_wheel", Properties.from(Blocks.IRON_BLOCK), SpinningWheelTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return SpinningWheelTile::new;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SpinningWheelTile()  ;
    }

}
