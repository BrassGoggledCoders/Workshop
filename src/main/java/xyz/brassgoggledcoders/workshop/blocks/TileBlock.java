package xyz.brassgoggledcoders.workshop.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TileBlock extends Block {
    private final Supplier<? extends WorkshopGUIMachine> tileSupplier;

    public TileBlock(Properties properties, Supplier<? extends WorkshopGUIMachine> tileSupplier) {
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!player.isCrouching()) {
            Workshop.LOGGER.warn("1");
            handleTileEntity(worldIn, pos, workshopGUIMachine -> workshopGUIMachine.onActivated(player, handIn, hit));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @SuppressWarnings("rawtypes")
    private void handleTileEntity(IWorld world, BlockPos pos, Consumer<WorkshopGUIMachine> tileEntityConsumer) {
        Optional.ofNullable(world.getTileEntity(pos))
                .filter(tileEntity -> tileEntity instanceof WorkshopGUIMachine)
                .map(WorkshopGUIMachine.class::cast)
                .ifPresent(tileEntityConsumer);
    }
}
