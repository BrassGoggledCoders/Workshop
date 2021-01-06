package xyz.brassgoggledcoders.workshop.capabilities;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.item.BottleItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BottleCapabilityProvider implements IFluidHandlerItem, ICapabilityProvider {
    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

    @Nonnull
    protected ItemStack container;

    public BottleCapabilityProvider(@Nonnull ItemStack container) {
        this.container = container;
    }

    @Nonnull
    @Override
    public ItemStack getContainer() {
        return container;
    }

    public boolean canFillFluidType(FluidStack fluid) {
        if (fluid.getFluid() == Fluids.WATER || fluid.getFluid() == WorkshopFluids.HONEY.getFluid()) {
            return true;
        }
        return !getFilledBottle(fluid).isEmpty();
    }

    @Nonnull
    public FluidStack getFluid() {
        Item item = container.getItem();
        if (item instanceof PotionItem && Potions.WATER.equals(PotionUtils.getPotionFromItem(container))) {
            return new FluidStack(Fluids.WATER, WorkshopFluids.BOTTLE_VOLUME);
        }
        else if (item instanceof HoneyBottleItem) {
            return new FluidStack(WorkshopFluids.HONEY.getFluid(), WorkshopFluids.BOTTLE_VOLUME);
        }
        else if (item instanceof BottleItem) {
            return new FluidStack(((BottleItem) item).getFluid(), WorkshopFluids.BOTTLE_VOLUME);
        }
        return FluidStack.EMPTY;
    }

    protected void setFluid(@Nonnull FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            container = new ItemStack(Items.GLASS_BOTTLE);
        } else {
            container = getFilledBottle(fluidStack);
        }
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return WorkshopFluids.BOTTLE_VOLUME;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.getAmount() < WorkshopFluids.BOTTLE_VOLUME || !getFluid().isEmpty() || !canFillFluidType(resource)) {
            return 0;
        }

        if (action.execute()) {
            setFluid(resource);
        }

        return WorkshopFluids.BOTTLE_VOLUME;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.getAmount() < WorkshopFluids.BOTTLE_VOLUME) {
            return FluidStack.EMPTY;
        }

        FluidStack fluidStack = getFluid();
        if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource)) {
            if (action.execute()) {
                setFluid(FluidStack.EMPTY);
            }
            return fluidStack;
        }

        return FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (container.getCount() != 1 || maxDrain < WorkshopFluids.BOTTLE_VOLUME) {
            return FluidStack.EMPTY;
        }

        FluidStack fluidStack = getFluid();
        if (!fluidStack.isEmpty()) {
            if (action.execute()) {
                setFluid(FluidStack.EMPTY);
            }
            return fluidStack;
        }

        return FluidStack.EMPTY;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(capability, holder);
    }

    @Nonnull
    public static ItemStack getFilledBottle(@Nonnull FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();

        if (fluid == Fluids.WATER) {
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);
        }
        else if (fluid == WorkshopFluids.HONEY.getFluid()) {
            return new ItemStack(Items.HONEY_BOTTLE);
        }
        else if (WorkshopItems.BOTTLES.get(fluid.getRegistryName()) != null) {
            return new ItemStack(WorkshopItems.BOTTLES.get(fluid.getRegistryName()).get());
        }

        return ItemStack.EMPTY;
    }
}