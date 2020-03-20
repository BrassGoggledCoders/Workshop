package xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace;

import javax.annotation.Nonnull;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockRotation;

import net.minecraft.block.Blocks;
import net.minecraft.util.BlockRenderLayer;
import xyz.brassgoggledcoders.workshop.Workshop;

public class SinteringFurnaceBlock extends BlockRotation<SinteringFurnaceTile> {

    public SinteringFurnaceBlock() {
        super("sintering_furnace", Properties.from(Blocks.FURNACE), SinteringFurnaceTile.class);
        setItemGroup(Workshop.workshopTab);
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IFactory<SinteringFurnaceTile> getTileEntityFactory() {
        return SinteringFurnaceTile::new;
    }

}
