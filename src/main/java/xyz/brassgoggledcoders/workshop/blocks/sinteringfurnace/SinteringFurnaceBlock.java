package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class SinteringFurnaceBlock extends BlockTileBase {

    public SinteringFurnaceBlock() {
        super("sintering_furnace", Properties.from(Blocks.FURNACE), SinteringFurnaceTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return SinteringFurnaceTile::new;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SinteringFurnaceTile();
    }

}
