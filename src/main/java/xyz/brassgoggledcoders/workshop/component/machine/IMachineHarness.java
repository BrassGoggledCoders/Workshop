package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import net.minecraft.entity.player.PlayerEntity;

public interface IMachineHarness<T extends IMachineHarness<T>> extends IComponentHarness {

    LocatorInstance getLocatorInstance();

    MachineComponent<T> getMachineComponent();

    boolean canInteractWith(PlayerEntity player);
}
