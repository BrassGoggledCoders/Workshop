package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.Blocks;

public class SinteringFurnaceBlock extends BlockTileBase {

    public SinteringFurnaceBlock() {
        super("sintering_furnace", Properties.from(Blocks.FURNACE), SinteringFurnaceTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return SinteringFurnaceTile::new;
    }

}
