package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import xyz.brassgoggledcoders.workshop.tileentity.BottleRackTileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BottleRackBlock extends RotatableTileBlock<BottleRackTileEntity> {

    public BottleRackBlock() {
        super(Properties.create(Material.GLASS).notSolid(), BottleRackTileEntity::new);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
