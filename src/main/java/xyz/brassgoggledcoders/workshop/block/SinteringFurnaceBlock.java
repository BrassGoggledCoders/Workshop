package xyz.brassgoggledcoders.workshop.block;

import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.tileentity.SinteringFurnaceTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class SinteringFurnaceBlock extends GUITileBlock<SinteringFurnaceTileEntity> {

    protected static final VoxelShape AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SinteringFurnaceBlock() {
        super(Properties.create(Material.GLASS).hardnessAndResistance(3.5F).setLightLevel((state) -> state.get(LIT) ? 13 : 0).notSolid(), SinteringFurnaceTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(LIT, false));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, LIT);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    @ParametersAreNonnullByDefault
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
            Direction direction = stateIn.get(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : d4;
            double d6 = rand.nextDouble() * 9.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof SinteringFurnaceTileEntity) {
            for (FacingUtil.Sideness side : FacingUtil.Sideness.values()) {
                SinteringFurnaceTileEntity f = (SinteringFurnaceTileEntity) tileentity;
                switch (side) {
                    case TOP:
                        f.getInputInventory().getFacingModes().put(side, IFacingComponent.FaceMode.NONE);
                        f.getPowderInventory().getFacingModes().put(side, IFacingComponent.FaceMode.ENABLED);
                        f.getOutputInventory().getFacingModes().put(side, IFacingComponent.FaceMode.NONE);
                        break;
                    case BOTTOM:
                        f.getInputInventory().getFacingModes().put(side, IFacingComponent.FaceMode.NONE);
                        f.getPowderInventory().getFacingModes().put(side, IFacingComponent.FaceMode.NONE);
                        f.getOutputInventory().getFacingModes().put(side, IFacingComponent.FaceMode.ENABLED);
                        break;
                    default:
                        f.getInputInventory().getFacingModes().put(side, IFacingComponent.FaceMode.ENABLED);
                        f.getOutputInventory().getFacingModes().put(side, IFacingComponent.FaceMode.ENABLED);
                        break;
                }
            }
        }
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entityIn;
            ItemStack stack = itemEntity.getItem();
            this.handleTileEntity(worldIn, pos, (tile) -> itemEntity.setItem(ItemHandlerHelper.insertItemStacked(tile.getPowderInventory(), stack, false)));
        }
    }
}
