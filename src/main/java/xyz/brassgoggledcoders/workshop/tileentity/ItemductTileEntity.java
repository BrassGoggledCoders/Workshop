package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.block.ItemductBlock;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import javax.annotation.Nonnull;

public class ItemductTileEntity extends BasicInventoryTileEntity<ItemductTileEntity> {

    protected final InventoryComponent<ItemductTileEntity> inv;
    protected int timer = 0;
    protected int interval = 20;

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
        compound.put("inv", inv.serializeNBT());
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        inv.deserializeNBT(compound.getCompound("inv"));
        return super.write(compound);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getWorld() != null && !this.getWorld().isRemote) {
            timer++;
            if(timer > interval) {
                timer = 0;
                Direction facing = this.getBlockState().get(ItemductBlock.FACING);
                BlockPos target = this.getPos().offset(facing);
                if(!this.getWorld().isAirBlock(target)) {
                    TileEntity tile = this.getWorld().getTileEntity(target);
                    if(tile != null) {
                        tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).ifPresent(otherInventory -> {
                            ItemStack stackInSlot = this.inv.getStackInSlot(0);
                            if(!stackInSlot.isEmpty() && ItemHandlerHelper.insertItemStacked(otherInventory, stackInSlot.copy().split(1), true).isEmpty()) {
                                ItemHandlerHelper.insertItemStacked(otherInventory, stackInSlot.split(1), false);
                            }
                        });
                    }
                }
            }
        }
    }
}
