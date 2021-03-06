package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.tileentity.SealedBarrelTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SealedBarrelBlock extends GUITileBlock<SealedBarrelTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public SealedBarrelBlock() {
        super(Properties.from(Blocks.OAK_PLANKS), SealedBarrelTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("capability"));
        if (!fluidStack.isEmpty()) {
            tooltip.add(new StringTextComponent("Fluid: " + fluidStack.getDisplayName()));
        }
        tooltip.add(new StringTextComponent(String.format("%d/%dmB", fluidStack.getAmount(), SealedBarrelTileEntity.tankCapacity)));
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        SeasoningBarrelBlock.playSound(player, worldIn, pos, state, SoundEvents.BLOCK_BARREL_OPEN);
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
