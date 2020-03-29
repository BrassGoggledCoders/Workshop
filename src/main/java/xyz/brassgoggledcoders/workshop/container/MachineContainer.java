package xyz.brassgoggledcoders.workshop.container;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.workshop.component.machine.IMachineHarness;
import xyz.brassgoggledcoders.workshop.component.machine.MachineComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopContainers;

import javax.annotation.Nonnull;
import java.util.Optional;

public class MachineContainer extends BasicInventoryContainer implements ILocatable {

    private IMachineHarness<?> machineHarness;

    public MachineContainer(IMachineHarness<?> machineHarness, PlayerInventory inventory, int id) {
        super(WorkshopContainers.MACHINE.get(), inventory, id, machineHarness.getMachineComponent().getAssetProvider());
        this.machineHarness = machineHarness;
        initInventory();
    }

    public void addSlots() {
        if (machineHarness.getMachineComponent().getMultiInventoryComponent() != null) {
            for (InventoryComponent<?> handler : machineHarness.getMachineComponent().getMultiInventoryComponent().getInventoryHandlers()) {
                int i = 0;
                for (int y = 0; y < handler.getYSize(); ++y) {
                    for (int x = 0; x < handler.getXSize(); ++x) {
                        addSlot(new SlotItemHandler(handler, i, handler.getXPos() + handler.getSlotPosition().apply(i).getLeft(), handler.getYPos() + handler.getSlotPosition().apply(i).getRight()));
                        ++i;
                    }
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return machineHarness.canInteractWith(player);
    }

    public IMachineHarness<?> getMachineHarness() {
        return machineHarness;
    }

    public MachineComponent<?> getMachine() {
        return this.getMachineHarness().getMachineComponent();
    }

    @Override
    public LocatorInstance getLocatorInstance() {
        return machineHarness.getLocatorInstance();
    }

    @Override
    public void addExtraSlots() {
        super.addExtraSlots();
        addSlots();
    }

    public static MachineContainer create(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        return Optional.ofNullable(LocatorFactory.readPacketBuffer(packetBuffer))
                .flatMap(locatorInstance -> locatorInstance.locale(inventory.player))
                .filter(value -> value instanceof IMachineHarness)
                .map(value -> (IMachineHarness<?>)value)
                .map(value -> new MachineContainer(value, inventory, id))
                .orElseThrow(() -> new IllegalStateException("Failed to Find Machine"));
    }
}