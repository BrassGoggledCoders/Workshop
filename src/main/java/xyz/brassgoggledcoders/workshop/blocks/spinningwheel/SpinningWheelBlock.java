package xyz.brassgoggledcoders.workshop.blocks.spinningwheel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.block.Blocks;

public class SpinningWheelBlock extends BasicTileBlock<SpinningWheelTile> {

    public SpinningWheelBlock() {
        super(Properties.from(Blocks.IRON_BLOCK), SpinningWheelTile.class);
    }

    @Override
    public IFactory<SpinningWheelTile> getTileEntityFactory() {
        return SpinningWheelTile::new;
    }

}
