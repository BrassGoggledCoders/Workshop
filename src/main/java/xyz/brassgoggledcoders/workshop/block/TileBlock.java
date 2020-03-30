package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.tileentity.BasicMachineTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TileBlock extends Block {
    private final Supplier<? extends BasicMachineTileEntity> tileSupplier;

    public TileBlock(Properties properties, Supplier<? extends BasicMachineTileEntity> tileSupplier) {
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
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!player.isCrouching()) {
            handleTileEntity(worldIn, pos, workshopGUIMachine -> workshopGUIMachine.onActivated(player, handIn, hit));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @SuppressWarnings("rawtypes")
    private void handleTileEntity(IWorld world, BlockPos pos, Consumer<BasicMachineTileEntity> tileEntityConsumer) {
        Optional.ofNullable(world.getTileEntity(pos))
                .filter(tileEntity -> tileEntity instanceof BasicMachineTileEntity)
                .map(BasicMachineTileEntity.class::cast)
                .ifPresent(tileEntityConsumer);
    }
}