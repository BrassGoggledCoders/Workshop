package xyz.brassgoggledcoders.workshop.block.pipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.stream.Collectors;

public class PipeBlock extends Block {
    public PipeBlock() {
        this(Properties.create(Material.GLASS));
    }

    public PipeBlock(Properties properties) {
        super(properties);
        BlockState defaultState = this.getStateContainer().getBaseState();
        SixWayBlock.FACING_TO_PROPERTY_MAP
                .values()
                .forEach(property -> defaultState.with(property, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        SixWayBlock.FACING_TO_PROPERTY_MAP
                .values()
                .forEach(builder::add);
    }

    public Map<BlockPos, Direction> getConnections(BlockState state, BlockPos blockPos, Direction ignored) {
        return SixWayBlock.FACING_TO_PROPERTY_MAP.entrySet()
                .stream()
                .filter(entry -> entry.getKey() != ignored && state.get(entry.getValue()))
                .collect(Collectors.toMap(entry -> blockPos.offset(entry.getKey()),
                        entry -> entry.getKey().getOpposite()));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        BlockState blockState = this.getDefaultState();
        for (Map.Entry<Direction, BooleanProperty> combo : SixWayBlock.FACING_TO_PROPERTY_MAP.entrySet()) {
            blockState.with(combo.getValue(), checkSide(context.getWorld(), context.getPos(), combo.getKey()));
        }
        return blockState;
    }

    private boolean checkSide(World world, BlockPos pos, Direction key) {
        BlockPos checkPos = pos.offset(key);
        BlockState checkState = world.getBlockState(pos);
        if (checkState.getBlock() == this) {
            return true;
        } else {
            return this.checkFluidHandler(world, checkPos, key.getOpposite());
        }
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world,
                                          BlockPos currentPos, BlockPos facingPos) {
        return state.with(SixWayBlock.FACING_TO_PROPERTY_MAP.get(facing), facingState.getBlock() == this ||
                this.checkFluidHandler(world, facingPos, facing.getOpposite()));
    }

    private boolean checkFluidHandler(IWorld world, BlockPos blockPos, Direction facing) {
        if (world.isAreaLoaded(blockPos, 0)) {
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity != null) {
                return tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).isPresent();
            }
        }
        return false;
    }
}
