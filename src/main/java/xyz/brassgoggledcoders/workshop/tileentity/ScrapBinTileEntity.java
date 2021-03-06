package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.brassgoggledcoders.workshop.component.machine.FixedSidedInventoryComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopConfig;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ScrapBinTileEntity extends BasicInventoryTileEntity<ScrapBinTileEntity> {
    public final InventoryComponent<ScrapBinTileEntity> inventoryComponent;
    public final InventoryComponent<ScrapBinTileEntity> scrapOutput;
    public final ProgressBarComponent<ScrapBinTileEntity> scrapValue;

    public ScrapBinTileEntity() {
        super(WorkshopBlocks.SCRAP_BIN.getTileEntityType());
        this.getMachineComponent().addInventory(this.inventoryComponent = new FixedSidedInventoryComponent<ScrapBinTileEntity>("inventory", 8, 16, 24, FixedSidedInventoryComponent.NOT_BOTTOM)
                .setRange(8, 3));
        this.getMachineComponent().addInventory(this.scrapOutput = new FixedSidedInventoryComponent<>("output", 150, 75, 1, FacingUtil.Sideness.BOTTOM));
        this.getMachineComponent().addProgressBar(this.scrapValue = new ProgressBarComponent<>(155, 8, WorkshopConfig.COMMON.itemsRequiredPerScrapBag.get()));
        this.inventoryComponent.setOnSlotChanged((stack, slotNum) -> {
            if (this.hasWorld() && ItemHandlerHelper.insertItem(scrapOutput, new ItemStack(WorkshopItems.SCRAP_BAG.get()), true).isEmpty()) {
                //TODO Caching
                int count = stack.getCount();
                for (int i = 0; i < this.inventoryComponent.getSlots(); i++) {
                    if (i != slotNum) {
                        ItemStack otherStack = this.inventoryComponent.getStackInSlot(i);
                        if (ItemStack.areItemsEqual(stack, otherStack)) {
                            count += otherStack.getCount();
                        }
                    }
                }
                //If we have more than a stack total
                if (count > 64) {
                    //Shrink by the amount we are over a stack
                    int diff = count - 64;
                    stack.shrink(diff);
                    //Add that value to the scrap counter
                    this.scrapValue.setProgress(Math.min(this.scrapValue.getProgress() + diff, this.scrapValue.getMaxProgress()));
                    this.scrapValue.tickBar();
                    ComposterBlock.playEvent(Objects.requireNonNull(this.getWorld()), this.getPos(), false);
                }
            }
        });
        this.scrapValue.setOnFinishWork(() -> {
            if(this.hasWorld()) {
                ComposterBlock.playEvent(Objects.requireNonNull(this.getWorld()), this.getPos(), true);
                ItemHandlerHelper.insertItem(scrapOutput, new ItemStack(WorkshopItems.SCRAP_BAG.get()), false);
            }
        });
    }

    @Override
    public ScrapBinTileEntity getSelf() {
        return this;
    }

    @Override
    public void read(@Nonnull BlockState state, CompoundNBT compound) {
        inventoryComponent.deserializeNBT(compound.getCompound("inventory"));
        scrapOutput.deserializeNBT(compound.getCompound("scrap"));
        scrapValue.deserializeNBT(compound.getCompound("scrapValue"));
        super.read(state, compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.put("inventory", inventoryComponent.serializeNBT());
        compound.put("scrap", scrapOutput.serializeNBT());
        compound.put("scrapValue", scrapValue.serializeNBT());
        return super.write(compound);
    }
}
