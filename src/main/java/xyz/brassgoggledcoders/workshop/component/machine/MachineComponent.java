package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
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
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
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
import java.util.function.Supplier;

public class MachineComponent<T extends IMachineHarness<T>> implements IScreenAddonProvider {
    private MultiInventoryComponent<T> multiInventoryComponent;
    private MultiProgressBarHandler<T> multiProgressBarHandler;
    private MultiTankComponent<T> multiTankComponent;
    private MultiButtonComponent multiButtonComponent;
    private MultiFilterComponent multiFilterComponent;

    private final T componentHarness;
    private final Supplier<BlockPos> posSupplier;

    public Supplier<Direction> facingSupplier = () -> Direction.NORTH;

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

    public void setFacingSupplier(Supplier<Direction> facingSupplier) {
        this.facingSupplier = facingSupplier;
    }

    public void addInventory(InventoryComponent<T> handler) {
        if (this.multiInventoryComponent == null) {
            this.multiInventoryComponent = new MultiInventoryComponent<T>();
        }

        this.multiInventoryComponent.add(handler.setComponentHarness(componentHarness));
    }

    public void addProgressBar(ProgressBarComponent<T> progressBarComponent) {
        if (this.multiProgressBarHandler == null) {
            this.multiProgressBarHandler = new MultiProgressBarHandler<T>();
        }

        this.multiProgressBarHandler.addBar(progressBarComponent.setComponentHarness(componentHarness));
    }

    public void addTank(FluidTankComponent<T> tank) {
        if (this.multiTankComponent == null) {
            this.multiTankComponent = new MultiTankComponent<T>();
        }

        this.multiTankComponent.add(tank.setComponentHarness(this.componentHarness));
    }

    public void addButton(ButtonComponent button) {
        if (this.multiButtonComponent == null) {
            this.multiButtonComponent = new MultiButtonComponent();
        }

        this.multiButtonComponent.addButton(button);
    }

    public void addFilter(IFilter<?> filter) {
        if (this.multiFilterComponent == null) {
            this.multiFilterComponent = new MultiFilterComponent();
        }

        this.multiFilterComponent.add(filter);
    }

    @Nonnull
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
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

    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>();
        if (this.multiInventoryComponent != null) {
            addons.addAll(this.multiInventoryComponent.getScreenAddons());
        }

        if (this.multiProgressBarHandler != null) {
            addons.addAll(this.multiProgressBarHandler.getScreenAddons());
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

        return addons;
    }

    public IAssetProvider getAssetProvider() {
        return IAssetProvider.DEFAULT_PROVIDER;
    }

    public void tick() {
        World world = this.componentHarness.getComponentWorld();
        BlockPos pos = this.posSupplier.get();
        if (!world.isRemote()) {
            if (this.multiProgressBarHandler != null) {
                this.multiProgressBarHandler.update();
            }

            if (world.getGameTime() % (long) this.getFacingHandlerWorkTime() == 0L) {
                if (this.multiInventoryComponent != null) {

                    for (InventoryComponent<T> inventoryHandler : this.multiInventoryComponent.getInventoryHandlers()) {
                        if (inventoryHandler instanceof IFacingComponent && ((IFacingComponent) inventoryHandler)
                                .work(world, pos, this.getFacingDirection(), this.getFacingHandlerWorkAmount())) {
                            break;
                        }
                    }
                }

                if (this.multiTankComponent != null) {

                    for (FluidTankComponent<T> tankComponent : this.multiTankComponent.getTanks()) {
                        if (tankComponent instanceof IFacingComponent && ((IFacingComponent) tankComponent)
                                .work(world, pos, this.getFacingDirection(), this.getFacingHandlerWorkAmount())) {
                            break;
                        }
                    }
                }
            }
        }

    }

    public int getFacingHandlerWorkTime() {
        return 10;
    }

    public int getFacingHandlerWorkAmount() {
        return 4;
    }

    public MultiButtonComponent getMultiButtonComponent() {
        return this.multiButtonComponent;
    }

    public Direction getFacingDirection() {
        return this.facingSupplier.get();
    }

    public IFacingComponent getHandlerFromName(String string) {
        Iterator var2;
        if (this.multiInventoryComponent != null) {
            var2 = this.multiInventoryComponent.getInventoryHandlers().iterator();

            while (var2.hasNext()) {
                InventoryComponent<T> handler = (InventoryComponent) var2.next();
                if (handler instanceof IFacingComponent && handler.getName().equalsIgnoreCase(string)) {
                    return (IFacingComponent) handler;
                }
            }
        }

        if (this.multiTankComponent != null) {
            var2 = this.multiTankComponent.getTanks().iterator();

            while (var2.hasNext()) {
                FluidTankComponent<T> fluidTankComponent = (FluidTankComponent) var2.next();
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
                        componentHarness.markComponentForUpdate();

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
                componentHarness.markComponentForUpdate();
            } else if (this.multiTankComponent != null && this.multiTankComponent.handleFacingChange(name, facing, faceMode)) {
                componentHarness.markComponentForUpdate();
            }
        } else if (this.multiButtonComponent != null) {
            this.multiButtonComponent.clickButton(id, playerEntity, compound);
        }

    }
}
