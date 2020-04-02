package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;
import xyz.brassgoggledcoders.workshop.tileentity.SealedBarrelTileEntity;

import javax.annotation.Nullable;
import java.util.List;

public class SealedBarrelBlock extends TileBlock<SealedBarrelTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public SealedBarrelBlock() {
        super(Properties.from(Blocks.OAK_LOG), SealedBarrelTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("capability"));
        tooltip.add(new StringTextComponent("Fluid: ").appendSibling(fluidStack.getDisplayName()));
        tooltip.add(new StringTextComponent(String.format("%d/%dmB", fluidStack.getAmount(), SealedBarrelTileEntity.tankCapacity)));
    }
}
