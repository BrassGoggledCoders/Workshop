package xyz.brassgoggledcoders.workshop.capabilities.pipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.brassgoggledcoders.workshop.block.pipe.PipeBlock;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PipeNetwork implements INBTSerializable<CompoundNBT> {
    private final World world;
    private UUID uniqueId;
    private int currentDepth;
    private int maxSearchDepth;
    private final Set<BlockPos> contained;
    private final Map<BlockPos, Direction> potentials;
    private final Map<BlockPos, Direction> outputs;
    private boolean complete;

    public PipeNetwork(World world) {
        this.world = world;
        this.potentials = Maps.newHashMap();
        this.outputs = Maps.newHashMap();
        this.contained = Sets.newHashSet();
    }

    public PipeNetwork(World world, Direction pumpDirection, BlockPos pumpPos, int maxSearchDepth) {
        this(world);
        this.uniqueId = UUID.randomUUID();
        this.currentDepth = 1;
        this.maxSearchDepth = maxSearchDepth;
        this.complete = false;
        this.potentials.put(pumpPos.offset(pumpDirection), pumpDirection.getOpposite());
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putUniqueId("uniqueId", uniqueId);
        compoundNBT.putInt("maxSearchDepth", maxSearchDepth);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.uniqueId = nbt.getUniqueId("uniqueId");
        this.maxSearchDepth = nbt.getInt("maxSearchDepth");
        this.potentials.clear();
    }

    public Map<BlockPos, Direction> findOutputs() {
        if (!complete) {
            find();
            currentDepth++;
            if (currentDepth >= maxSearchDepth) {
                complete = true;
            }
        }
        return outputs;
    }

    private void find() {
        Map<BlockPos, Direction> newPotentials = Maps.newHashMap();
        for (Map.Entry<BlockPos, Direction> potential : potentials.entrySet()) {
            BlockState blockState = world.getBlockState(potential.getKey());
            if (blockState.getBlock() instanceof PipeBlock) {
                newPotentials.putAll(((PipeBlock) blockState.getBlock()).getConnections(blockState, potential.getKey(),
                        potential.getValue()));
            } else if (!blockState.isAir(world, potential.getKey())){
                TileEntity tileEntity = world.getTileEntity(potential.getKey());
                if (tileEntity != null) {
                    LazyOptional<IFluidHandler> fluidHandler = tileEntity.getCapability(
                            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, potential.getValue());
                    if (fluidHandler.isPresent()) {
                        outputs.put(potential.getKey(), potential.getValue());
                    }
                }
            }
        }
        this.potentials.clear();
        this.potentials.putAll(newPotentials);
    }
}
