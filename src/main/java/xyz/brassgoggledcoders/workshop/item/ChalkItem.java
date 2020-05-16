package xyz.brassgoggledcoders.workshop.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.tileentity.GUITile;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class ChalkItem extends BlockItem {

    public ChalkItem(Properties propertiesIn) {
        super(WorkshopBlocks.CHALK_WRITING.getBlock(), propertiesIn);
    }

    @Override
    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        boolean flag = super.onBlockPlaced(pos, worldIn, player, stack, state);
        if (!worldIn.isRemote && !flag && player != null) {
            //TODO Ideally remove null...
            handleTileEntity(worldIn, pos, tile -> tile.onActivated(player, player.getActiveHand(), null));
        }
        return flag;
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, Consumer<? super GUITile> tileEntityConsumer) {
        Optional.ofNullable(world.getTileEntity(pos))
                .filter(tileEntity -> tileEntity instanceof GUITile)
                .map(GUITile.class::cast)
                .ifPresent(tileEntityConsumer);
    }
}
