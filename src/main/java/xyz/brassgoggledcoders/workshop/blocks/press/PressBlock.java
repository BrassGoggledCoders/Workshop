package xyz.brassgoggledcoders.workshop.blocks.press;


import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.Blocks;

public class PressBlock extends BlockTileBase {

    public PressBlock() {
        super("press", Properties.from(Blocks.IRON_BLOCK), PressTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return PressTile::new;
    }
}
