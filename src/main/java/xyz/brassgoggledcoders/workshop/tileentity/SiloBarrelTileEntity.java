package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import javax.annotation.Nonnull;

public class SiloBarrelTileEntity extends BasicInventoryTileEntity<SiloBarrelTileEntity> {

    protected final InventoryComponent<SiloBarrelTileEntity> inventoryComponent;
    protected int timer = 0;
    protected final int interval = 20;

    public SiloBarrelTileEntity() {
        super(WorkshopBlocks.SILO_BARREL.getTileEntityType());
        this.getMachineComponent().addInventory(this.inventoryComponent = new InventoryComponent<SiloBarrelTileEntity>("inventory", 5, 20, 27)
                .setRange(9, 3));
    }

    @Override
    public SiloBarrelTileEntity getSelf() {
        return this;
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        inventoryComponent.deserializeNBT(compound.getCompound("capability"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("capability", inventoryComponent.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            timer++;
            if (timer > interval) {
                timer = 0;
                TileEntity tile = this.getWorld().getTileEntity(this.getPos().down());
                if (tile != null) {
                    tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).ifPresent(otherInventory -> {
                        for (int i = 0; i < this.inventoryComponent.getSlots(); i++) {
                            ItemStack stackInSlot = this.inventoryComponent.getStackInSlot(i);
                            if (!stackInSlot.isEmpty() && ItemHandlerHelper.insertItemStacked(otherInventory, stackInSlot.copy().split(1), true).isEmpty()) {
                                ItemHandlerHelper.insertItemStacked(otherInventory, stackInSlot.split(1), false);
                                break;
                            }
                        }
                    });
                }
            }
        }
    }
}