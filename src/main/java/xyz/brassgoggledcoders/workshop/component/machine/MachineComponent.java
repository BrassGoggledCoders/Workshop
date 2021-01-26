package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.button.MultiButtonComponent;
import com.hrznstudio.titanium.component.filter.MultiFilterComponent;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.MultiTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.MultiInventoryComponent;
import com.hrznstudio.titanium.component.progress.MultiProgressBarHandler;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MachineComponent<T extends IMachineHarness<T>> implements IScreenAddonProvider, IContainerAddonProvider,
        INBTSerializable<CompoundNBT> {
    protected final T componentHarness;
    protected final Supplier<BlockPos> posSupplier;
    protected MultiInventoryComponent<T> multiInventoryComponent;
    protected MultiTankComponent<T> multiTankComponent;
    protected MultiButtonComponent multiButtonComponent;
    protected MultiFilterComponent multiFilterComponent;
    protected MultiProgressBarHandler<T> multiProgressBarHandler;
    protected Function<BlockState, Direction> facingFunction = state -> Direction.NORTH;

    public MachineComponent(T componentHarness, Supplier<BlockPos> posSupplier) {
        this.componentHarness = componentHarness;
        this.posSupplier = posSupplier;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public ActionResultType onActivated(PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (this.multiTankComponent != null) {
            return this.multiTankComponent.getCapabilityForSide(null)
                    .map(value -> (IFluidHandler) value)
                    .map(value -> FluidUtil.interactWithFluidHandler(player, hand, value))
                    .map(value -> value ? ActionResultType.SUCCESS : ActionResultType.PASS)
                    .orElse(ActionResultType.PASS);
        }
        return ActionResultType.PASS;
    }

    public void setFacingFunction(Function<BlockState, Direction> facingFunction) {
        this.facingFunction = facingFunction;
    }

    public void addProgressBar(ProgressBarComponent<T> progressBarComponent) {
        if (this.multiProgressBarHandler == null) {
            this.multiProgressBarHandler = new MultiProgressBarHandler<>();
        }
        this.multiProgressBarHandler.add(progressBarComponent.setComponentHarness(componentHarness));
    }

    public void addInventory(InventoryComponent<T> handler) {
        if (this.multiInventoryComponent == null) {
            this.multiInventoryComponent = new MultiInventoryComponent<>();
        }
        this.multiInventoryComponent.add(handler.setComponentHarness(componentHarness));
    }

    public void addTank(FluidTankComponent<T> tank) {
        if (this.multiTankComponent == null) {
            this.multiTankComponent = new MultiTankComponent<>();
        }
        this.multiTankComponent.add(tank.setComponentHarness(this.componentHarness));
    }

    public void addButton(ButtonComponent button) {
        if (this.multiButtonComponent == null) {
            this.multiButtonComponent = new MultiButtonComponent();
        }
        this.multiButtonComponent.add(button);
    }

    public void addFilter(IFilter<?> filter) {
        if (this.multiFilterComponent == null) {
            this.multiFilterComponent = new MultiFilterComponent();
        }
        this.multiFilterComponent.add(filter);
    }

    @Nonnull
    public <V> LazyOptional<V> getCapability(@Nonnull Capability<V> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.multiInventoryComponent != null) {
            return this.multiInventoryComponent.getCapabilityForSide(FacingUtil.getFacingRelative(this.getFacingDirection(), side)).cast();
        } else {
            return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this.multiTankComponent != null ?
                    this.multiTankComponent.getCapabilityForSide(FacingUtil.getFacingRelative(this.getFacingDirection(), side)).cast() : LazyOptional.empty();
        }
    }

    public MultiInventoryComponent<T> getMultiInventoryComponent() {
        return this.multiInventoryComponent;
    }

    @Nonnull
    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        if (this.multiInventoryComponent != null) {
            addons.addAll(this.multiInventoryComponent.getScreenAddons());
        }
        if (this.multiTankComponent != null) {
            addons.addAll(this.multiTankComponent.getScreenAddons());
        }
        if (this.multiButtonComponent != null) {
            addons.addAll(this.multiButtonComponent.getScreenAddons());
        }
        if (this.multiFilterComponent != null) {
            addons.addAll(this.multiFilterComponent.getScreenAddons());
        }
        if (this.multiProgressBarHandler != null) {
            addons.addAll(this.multiProgressBarHandler.getScreenAddons());
        }
        return addons;
    }

    @Nonnull
    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>();
        if (this.multiInventoryComponent != null) {
            addons.addAll(this.multiInventoryComponent.getContainerAddons());
        }
        if (this.multiTankComponent != null) {
            addons.addAll(this.multiTankComponent.getContainerAddons());
        }
        if (this.multiProgressBarHandler != null) {
            addons.addAll(this.multiProgressBarHandler.getContainerAddons());
        }
        return addons;
    }

    public MultiButtonComponent getMultiButtonComponent() {
        return this.multiButtonComponent;
    }

    public Direction getFacingDirection() {
        return this.facingFunction.apply(componentHarness.getComponentWorld().getBlockState(this.posSupplier.get()));
    }

    public IFacingComponent getHandlerFromName(String string) {
        Iterator<InventoryComponent<T>> var2;
        if (this.multiInventoryComponent != null) {
            var2 = this.multiInventoryComponent.getInventoryHandlers().iterator();

            while (var2.hasNext()) {
                InventoryComponent<T> handler = var2.next();
                if (handler instanceof IFacingComponent && handler.getName().equalsIgnoreCase(string)) {
                    return (IFacingComponent) handler;
                }
            }
        }

        if (this.multiTankComponent != null) {
            for (FluidTankComponent<T> fluidTankComponent : this.multiTankComponent.getTanks()) {
                if (fluidTankComponent instanceof IFacingComponent && fluidTankComponent.getName().equalsIgnoreCase(string)) {
                    return (IFacingComponent) fluidTankComponent;
                }
            }
        }

        return null;
    }

    public void handleButtonMessage(int id, PlayerEntity playerEntity, CompoundNBT compound) {
        String name;
        if (id == -2) {
            name = compound.getString("Name");
            if (this.multiFilterComponent != null) {

                for (IFilter<?> iFilter : this.multiFilterComponent.getFilters()) {
                    if (iFilter.getName().equals(name)) {
                        int slot = compound.getInt("Slot");
                        iFilter.setFilter(slot, ItemStack.read(compound.getCompound("Filter")));
                        componentHarness.markComponentForUpdate(false);

                        break;
                    }
                }
            }
        }

        if (id == -1) {
            name = compound.getString("Name");
            FacingUtil.Sideness facing = FacingUtil.Sideness.valueOf(compound.getString("Facing"));
            IFacingComponent.FaceMode faceMode = IFacingComponent.FaceMode.values()[compound.getInt("Next")];
            if (this.multiInventoryComponent != null && this.multiInventoryComponent.handleFacingChange(name, facing, faceMode)) {
                componentHarness.markComponentForUpdate(false);
            } else if (this.multiTankComponent != null && this.multiTankComponent.handleFacingChange(name, facing, faceMode)) {
                componentHarness.markComponentForUpdate(false);
            }
        } else if (this.multiButtonComponent != null) {
            this.multiButtonComponent.clickButton(id, playerEntity, compound);
        }

    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
    }

    public void tick() {
    }
}
