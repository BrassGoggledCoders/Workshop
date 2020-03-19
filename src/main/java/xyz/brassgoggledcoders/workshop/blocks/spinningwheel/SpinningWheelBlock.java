package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;

import net.minecraft.block.Blocks;

public class SpinningWheelBlock extends BlockTileBase<SpinningWheelTile> {

    public SpinningWheelBlock() {
        super("spinning_wheel", Properties.from(Blocks.IRON_BLOCK), SpinningWheelTile.class);
    }

    @Override
    public IFactory<SpinningWheelTile> getTileEntityFactory() {
        return SpinningWheelTile::new;
    }

}
