package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.tileentity.SeasoningBarrelTileEntity;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicReference;

public class SeasoningBarrelBlock extends GUITileBlock<SeasoningBarrelTileEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public SeasoningBarrelBlock() {
        super(Properties.from(Blocks.OAK_PLANKS).notSolid(), SeasoningBarrelTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace());
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        playSound(player, worldIn, pos, state, SoundEvents.BLOCK_BARREL_OPEN);
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    public static void playSound(PlayerEntity player, World world, BlockPos pos, BlockState state, SoundEvent sound) {
        Vector3i vector3i = state.get(FACING).getDirectionVec();
        double d0 = (double)pos.getX() + 0.5D + (double)vector3i.getX() / 2.0D;
        double d1 = (double)pos.getY() + 0.5D + (double)vector3i.getY() / 2.0D;
        double d2 = (double)pos.getZ() + 0.5D + (double)vector3i.getZ() / 2.0D;
        world.playSound(player, d0, d1, d2, sound, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
    }
}
