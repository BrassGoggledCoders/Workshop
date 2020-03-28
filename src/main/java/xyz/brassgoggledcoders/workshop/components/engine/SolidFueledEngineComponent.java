package xyz.brassgoggledcoders.workshop.components.engine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.BooleanSupplier;

public class SolidFueledEngineComponent extends EngineComponent implements INBTSerializable<IntNBT> {
    private int burnTime = 0;
    private final IItemHandlerModifiable fuelInventory;
    private final int fuelSlot;
    private final BooleanSupplier shouldContinue;

    public SolidFueledEngineComponent(IItemHandlerModifiable fuelInventory, int fuelSlot, BooleanSupplier shouldContinue) {
        this.fuelInventory = fuelInventory;
        this.fuelSlot = fuelSlot;
        this.shouldContinue = shouldContinue;
    }

    public void tick() {
        if (burnTime > 0) {
            burnTime--;
        }
        if (burnTime <= 0) {
            burnTime = 0;
            if (shouldContinue.getAsBoolean()) {
                ItemStack itemStack = fuelInventory.getStackInSlot(fuelSlot);
                if (!itemStack.isEmpty()) {
                    int newBurnTime = ForgeHooks.getBurnTime(itemStack) * 2;
                    if (newBurnTime > 0) {
                        if (itemStack.hasContainerItem()) {
                            fuelInventory.setStackInSlot(0, itemStack.getContainerItem());
                        } else {
                            itemStack.shrink(1);
                        }
                        burnTime += newBurnTime;
                    }
                }
            }
        }
    }

    public boolean isRunning() {
        return burnTime > 0;
    }

    @Override
    public IntNBT serializeNBT() {
        return IntNBT.valueOf(burnTime);
    }

    @Override
    public void deserializeNBT(IntNBT nbt) {
        burnTime = nbt.getInt();
    }
}
