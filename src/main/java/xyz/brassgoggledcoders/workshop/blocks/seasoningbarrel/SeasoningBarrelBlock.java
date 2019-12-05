package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.Blocks;

public class SeasoningBarrelBlock extends BlockTileBase {
    public SeasoningBarrelBlock() {
        super("seasoning_barrel", Properties.from(Blocks.IRON_BLOCK), SeasoningBarrelTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return SeasoningBarrelTile::new;
    }

}
