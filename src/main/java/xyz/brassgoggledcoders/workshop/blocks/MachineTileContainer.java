package xyz.brassgoggledcoders.workshop.blocks;

import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopContainers;

import javax.annotation.Nonnull;

public class MachineTileContainer<T extends WorkshopGUIMachine<T>> extends BasicInventoryContainer implements ILocatable {

    private T tile;

    public MachineTileContainer(T tile, PlayerInventory inventory, int id) {
        super(WorkshopContainers.MACHINE.get(), inventory, id, tile.getMachineComponent().getAssetProvider());
        this.tile = tile;
        initInventory();
    }

    public void addTileSlots() {
        if (tile.getMachineComponent().getMultiInventoryComponent() != null) {
            for (InventoryComponent<T> handler : tile.getMachineComponent().getMultiInventoryComponent().getInventoryHandlers()) {
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

    public void updateSlotPosition() {
        if (tile.getMachineComponent().getMultiInventoryComponent() != null) {
            for (InventoryComponent<T> handler : tile.getMachineComponent().getMultiInventoryComponent().getInventoryHandlers()) {
                int i = 0;
                for (int y = 0; y < handler.getYSize(); ++y) {
                    for (int x = 0; x < handler.getXSize(); ++x) {
                        for (Slot inventorySlot : this.inventorySlots) {
                            if (!(inventorySlot instanceof SlotItemHandler)) continue;
                            if (((SlotItemHandler) inventorySlot).getItemHandler().equals(handler) && i == inventorySlot.getSlotIndex()) {
                                //inventorySlot.xPos = handler.getXPos() + handler.getSlotPosition().apply(i).getLeft();
                                //inventorySlot.yPos = handler.getYPos() + handler.getSlotPosition().apply(i).getRight();
                                break;
                            }
                        }
                        ++i;
                    }
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        return true;
    }

    public T getTile() {
        return tile;
    }

    @Override
    public LocatorInstance getLocatorInstance() {
        return new TileEntityLocatorInstance(tile.getPos());
    }

    @Override
    public void addExtraSlots() {
        super.addExtraSlots();
        addTileSlots();
    }

    public static MachineTileContainer<?> create(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        TileEntity tileEntity = inventory.player.world.getTileEntity(packetBuffer.readBlockPos());
        if (tileEntity instanceof WorkshopGUIMachine) {
            return new MachineTileContainer((WorkshopGUIMachine<?>) tileEntity, inventory, id);
        }
        throw new IllegalStateException("Failed to Find Tile Entity");
    }
}