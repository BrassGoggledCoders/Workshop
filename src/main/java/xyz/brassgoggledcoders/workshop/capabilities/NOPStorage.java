package xyz.brassgoggledcoders.workshop.capabilities;


import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class NOPStorage<CAP> implements Capability.IStorage<CAP> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<CAP> capability, CAP instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<CAP> capability, CAP instance, Direction side, INBT nbt) {

    }
}