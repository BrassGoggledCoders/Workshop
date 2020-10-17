package xyz.brassgoggledcoders.workshop.block;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.tileentity.BasicInventoryTileEntity;
import xyz.brassgoggledcoders.workshop.tileentity.BasicMachineTileEntity;
import xyz.brassgoggledcoders.workshop.tileentity.GUITile;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GUITileBlock<T extends TileEntity & GUITile> extends TileBlock<T> {
    public GUITileBlock(Properties properties, Supplier<T> tileSupplier) {
        super(properties,tileSupplier);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
        handleTileEntity(worldIn, pos, tile -> result.set(tile.onActivated(player, handIn, hit)));
        return result.get();
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, Consumer<? super GUITile> tileEntityConsumer) {
        Optional.ofNullable(world.getTileEntity(pos))
                .filter(tileEntity -> tileEntity instanceof GUITile)
                .map(GUITile.class::cast)
                .ifPresent(tileEntityConsumer);
    }
}