package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.tileentity.GUITile;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GUITileBlock<T extends TileEntity & GUITile> extends TileBlock<T> {
    public GUITileBlock(Properties properties, Supplier<T> tileSupplier) {
        super(properties, tileSupplier);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        AtomicReference<ActionResultType> result = new AtomicReference<>(ActionResultType.PASS);
        handleTileEntity(worldIn, pos, tile -> result.set(tile.onActivated(player, handIn, hit)));
        return result.get();
    }

    protected void handleTileEntity(IWorld world, BlockPos pos, Consumer<T> tileEntityConsumer) {
        Optional.ofNullable(world.getTileEntity(pos))
                //Is there a cleaner way to do this?
                .filter(tileEntity -> tileEntity.getType().equals(this.tileSupplier.get().getType()))
                .map(tileEntity -> (T) tileEntity)
                .ifPresent(tileEntityConsumer);
    }
}