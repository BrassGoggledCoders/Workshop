package xyz.brassgoggledcoders.workshop.api.capabilities;


import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import xyz.brassgoggledcoders.workshop.content.WorkshopCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FurnaceCapabilityProvider implements ICapabilityProvider {

    final FurnaceTileEntity tile;

    public FurnaceCapabilityProvider(FurnaceTileEntity tile) {
        this.tile = tile;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == WorkshopCapabilities.COLLECTOR_TARGET ? LazyOptional.of(() -> new FurnaceCollectorTarget(tile)).cast() : LazyOptional.empty();
    }
}