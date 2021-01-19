package xyz.brassgoggledcoders.workshop.block;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.tileentity.BasicInventoryTileEntity;
import xyz.brassgoggledcoders.workshop.tileentity.BasicMachineTileEntity;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TileBlock<T extends TileEntity> extends Block {
    protected final Supplier<T> tileSupplier;

    public TileBlock(Properties properties, Supplier<T> tileSupplier) {
        super(properties);
        this.tileSupplier = tileSupplier;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return this.tileSupplier.get();
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof BasicMachineTileEntity) {
                ((BasicMachineTileEntity<?, ?>) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof BasicInventoryTileEntity) {
                for (InventoryComponent<?> inventoryComponent : ((BasicInventoryTileEntity<?>) tileentity).getMachineComponent().getMultiInventoryComponent().getInventoryHandlers()) {
                    InventoryUtil.getItemStackStream(inventoryComponent).forEach(stack -> InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
                }
                worldIn.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
}
