package xyz.brassgoggledcoders.workshop.capabilities.pipe;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public class PipeNetworkCapability implements INBTSerializable<CompoundNBT> {
    @CapabilityInject(PipeNetworkCapability.class)
    public static Capability<PipeNetworkCapability> CAP;

    private final Table<BlockPos, Direction, PipeNetwork> pumpNetworks;
    private final World world;

    public PipeNetworkCapability(World world) {
        this.world = world;
        this.pumpNetworks = HashBasedTable.create();
    }

    public Map<BlockPos, Direction> getPotentialOutputs(BlockPos pumpPos, Direction pumpDirection) {
        PipeNetwork pipeNetwork = pumpNetworks.get(pumpPos, pumpDirection);
        if (pipeNetwork == null) {
            pipeNetwork = new PipeNetwork(world, pumpDirection, pumpPos, 25);
            pumpNetworks.put(pumpPos, pumpDirection, pipeNetwork);
        }

        return pipeNetwork.findOutputs();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();

        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
