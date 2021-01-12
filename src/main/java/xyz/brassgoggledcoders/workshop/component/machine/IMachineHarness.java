package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nonnull;
import java.util.List;

public interface IMachineHarness<T extends IMachineHarness<T>> extends IComponentHarness, IScreenAddonProvider,
        IContainerAddonProvider {

    LocatorInstance getLocatorInstance();

    void createMachineComponent(MachineComponent<T> machineComponent);

    MachineComponent<T> getMachineComponent();

    boolean canInteractWith(PlayerEntity player);

    @Nonnull
    @Override
    default List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return this.getMachineComponent().getScreenAddons();
    }

    @Nonnull
    @Override
    default List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return this.getMachineComponent().getContainerAddons();
    }

}
