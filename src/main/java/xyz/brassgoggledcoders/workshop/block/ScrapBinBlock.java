package xyz.brassgoggledcoders.workshop.block;

import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.tileentity.ScrapBinTileEntity;

import javax.annotation.Nonnull;

public class ScrapBinBlock extends GUITileBlock<ScrapBinTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ScrapBinBlock() {
        super(Properties.from(Blocks.CHEST), ScrapBinTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ScrapBinTileEntity) {
            for (FacingUtil.Sideness side : FacingUtil.Sideness.values()) {
                if (FacingUtil.Sideness.BOTTOM.equals(side)) {
                    ((ScrapBinTileEntity) tileentity).inventoryComponent.getFacingModes().put(FacingUtil.Sideness.BOTTOM, IFacingComponent.FaceMode.NONE);
                    ((ScrapBinTileEntity) tileentity).scrapOutput.getFacingModes().put(FacingUtil.Sideness.BOTTOM, IFacingComponent.FaceMode.ENABLED);
                } else {
                    ((ScrapBinTileEntity) tileentity).scrapOutput.getFacingModes().put(side, IFacingComponent.FaceMode.NONE);
                    ((ScrapBinTileEntity) tileentity).inventoryComponent.getFacingModes().put(side, IFacingComponent.FaceMode.ENABLED);
                }
            }
        }
    }

    @Override
    protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }
}
