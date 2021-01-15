package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.block.ItemductBlock;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import javax.annotation.Nonnull;

public class ItemductTileEntity extends BasicInventoryTileEntity<ItemductTileEntity> {

    protected final InventoryComponent<ItemductTileEntity> inv;
    protected int timer = 0;
    protected int interval = 20;
    private LazyOptional<IItemHandler> capability;

    public ItemductTileEntity() {
        super(WorkshopBlocks.ITEMDUCT.getTileEntityType());
        this.getMachineComponent().addInventory(this.inv = new InventoryComponent<>("inv", 80, 20, 1));
    }

    @Override
    public ItemductTileEntity getSelf() {
        return this;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        inv.deserializeNBT(compound.getCompound("inv"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("inv", inv.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            timer++;
            if (timer > interval) {
                timer = 0;
                if (capability == null) {
                    Direction facing = this.getBlockState().get(ItemductBlock.FACING);
                    BlockPos target = this.getPos().offset(facing);
                    TileEntity tile = this.getWorld().getTileEntity(target);
                    if (tile != null) {
                        capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
                        capability.addListener((t) -> this.invalidateCache());
                    } else {
                        capability = null;
                    }
                } else {
                    capability.ifPresent(otherInventory -> {
                        ItemStack stackInSlot = this.inv.getStackInSlot(0);
                        if (!stackInSlot.isEmpty() && ItemHandlerHelper.insertItemStacked(otherInventory, stackInSlot.copy().split(1), true).isEmpty()) {
                            ItemHandlerHelper.insertItemStacked(otherInventory, stackInSlot.split(1), false);
                        }
                    });
                }
            }
        }
    }

    public void invalidateCache() {
        this.capability = null;
    }
}
