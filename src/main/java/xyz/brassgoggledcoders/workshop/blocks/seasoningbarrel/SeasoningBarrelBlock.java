package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class SeasoningBarrelBlock extends BlockTileBase {


    public SeasoningBarrelBlock() {
        super("seasoning_barrel", Properties.from(Blocks.IRON_BLOCK), SeasoningBarrelTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return SeasoningBarrelTile::new;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SeasoningBarrelTile();
    }

}
