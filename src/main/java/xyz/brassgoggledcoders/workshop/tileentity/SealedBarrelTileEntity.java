package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidUtil;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

public class SealedBarrelTileEntity extends FluidFillingTileEntity<SealedBarrelTileEntity> {

    private final InventoryComponent<SealedBarrelTileEntity> drainingInventory;

    @SuppressWarnings("unchecked")
    public SealedBarrelTileEntity() {
        super(WorkshopBlocks.SEALED_BARREL.getTileEntityType(), 1);
        this.getMachineComponent().addInventory(this.drainingInventory = new InventoryComponent<SealedBarrelTileEntity>("draining", 50, 50, 1)
                .setInputFilter((stack, integer) -> FluidUtil.getFluidHandler(stack).isPresent()));
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        drainingInventory.deserializeNBT(compound.getCompound("draining"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("draining", drainingInventory.serializeNBT());
        return super.write(compound);
    }

    @Override
    public SealedBarrelTileEntity getSelf() {
        return this;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            InventoryUtil.getItemStackStream(this.drainingInventory).forEach(stack -> {
                if (!stack.isEmpty()) {
                    if (FluidUtil.tryEmptyContainer(stack, this.tank, fluidMovedPerTick, null, false).isSuccess()) {
                        this.drainingInventory.setStackInSlot(0, FluidUtil.tryEmptyContainer(stack, this.tank, fluidMovedPerTick, null, true).getResult());
                    }
                }
            });
        }
    }
}
