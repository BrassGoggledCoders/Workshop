package xyz.brassgoggledcoders.workshop.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicCapabilityProvider<T extends INBTSerializable<U>, U extends INBT> implements ICapabilitySerializable<U> {
    private final Capability<T> capability;
    private final LazyOptional<T> lazyOptional;
    private final NonNullSupplier<U> nbtSupplier;

    public BasicCapabilityProvider(Capability<T> capability, LazyOptional<T> lazyOptional, NonNullSupplier<U> supplier) {
        this.capability = capability;
        this.lazyOptional = lazyOptional;
        this.nbtSupplier = supplier;
    }

    @Nonnull
    @Override
    public <V> LazyOptional<V> getCapability(@Nonnull Capability<V> cap, @Nullable Direction side) {
        return capability.orEmpty(cap, lazyOptional);
    }

    @Override
    public U serializeNBT() {
        return lazyOptional.map(this::handleNBT)
                .orElseGet(nbtSupplier);
    }

    private  U handleNBT(T input) {
        U value = input.serializeNBT();
        if (value == null) {
            return nbtSupplier.get();
        } else {
            return value;
        }
    }

    @Override
    public void deserializeNBT(U nbt) {
        lazyOptional.ifPresent(value -> value.deserializeNBT(nbt));
    }
}
