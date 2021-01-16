package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
public class BellowsBlock extends Block {

    protected static final VoxelShape UNPRESSED_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    protected static final VoxelShape PRESSED_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public static final BooleanProperty PRESSED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BellowsBlock() {
        super(Properties.from(Blocks.BROWN_WOOL));
        this.setDefaultState((this.stateContainer.getBaseState().with(PRESSED, false).with(FACING, Direction.NORTH)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, PRESSED);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(PRESSED) ? PRESSED_AABB : UNPRESSED_AABB;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        BlockState state = worldIn.getBlockState(pos);
        if (!worldIn.isRemote && !state.get(PRESSED)) {
            this.updateState(worldIn, pos, state, true);
            worldIn.playSound(null, pos, SoundEvents.ENTITY_CAT_HISS, SoundCategory.BLOCKS, 1.0F, 1.0F);
            Direction facing = state.get(FACING);
            BlockPos offsetPos = pos.offset(facing);
            if (worldIn.getTileEntity(offsetPos) instanceof AbstractFurnaceTileEntity) {
                AbstractFurnaceTileEntity furnace = (AbstractFurnaceTileEntity) worldIn.getTileEntity(pos.offset(state.get(FACING)));
                if (furnace != null) {
                    furnace.cookTime += 20;
                    //Prevent overflowing as the furnace doesn't do that itself.
                    if (furnace.cookTime >= furnace.cookTimeTotal) {
                        furnace.cookTime = furnace.cookTimeTotal - 1; //Furnace does an == check not an >= check.
                    }
                }
            } else if (worldIn.isAirBlock(offsetPos)) {
                List<Entity> entityList = worldIn.getEntitiesWithinAABB(Entity.class,
                        new AxisAlignedBB(offsetPos), entity -> entity instanceof ItemEntity || entity.canBePushed());
                entityList.forEach(entity -> entity.setMotion(randomise(worldIn, facing.getXOffset() * 0.2),
                        worldIn.getRandom().nextDouble() * 0.1D, randomise(worldIn, facing.getZOffset() * 0.2)));
            }
        }
    }

    private double randomise(World worldIn, double start) {
        return start + 0.5D + ((double) worldIn.rand.nextFloat() - 0.5D) * 2.0D;
    }

    protected void updateState(World worldIn, BlockPos pos, BlockState state, boolean pressed) {
        BlockState blockstate = state.with(PRESSED, pressed);
        worldIn.setBlockState(pos, blockstate, 2);
        worldIn.notifyNeighborsOfStateChange(pos, this);
        worldIn.markBlockRangeForRenderUpdate(pos, state, blockstate);
        worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(pos), this, this.tickRate());
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(PRESSED)) {
            for (int i = 0; i < 20 + worldIn.rand.nextInt(10); i++) {
                worldIn.addParticle(ParticleTypes.SMOKE, randomise(worldIn, pos.getX()), randomise(worldIn, pos.getY()), randomise(worldIn, pos.getZ()),
                        randomise(worldIn, stateIn.get(FACING).getDirectionVec().getX()) * 0.01, worldIn.getRandom().nextDouble() * 0.01, randomise(worldIn, stateIn.get(FACING).getDirectionVec().getZ()) * 0.01);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(PRESSED)) {
            this.updateState(worldIn, pos, state, false);
        }
    }

    //@Override
    public int tickRate() {
        return 5; //Quarter of a second, should be quick enough to make it smooth when jumped on
    }
}
