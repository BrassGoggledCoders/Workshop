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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.tileentity.CollectorTileEntity;
import xyz.brassgoggledcoders.workshop.tileentity.SealedBarrelTileEntity;

import javax.annotation.Nullable;
import java.util.List;

public class CollectorBlock extends TileBlock<CollectorTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public CollectorBlock() {
        super(Properties.from(Blocks.FURNACE), CollectorTileEntity::new);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        //TODO Potentially unsafe
        this.handleTileEntity(worldIn, pos, tile -> ((CollectorTileEntity) tile).getMachineComponent().forceRecipeRecheck());
    }
}
