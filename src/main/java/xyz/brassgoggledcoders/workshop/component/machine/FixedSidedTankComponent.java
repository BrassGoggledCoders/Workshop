package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.SidedFluidTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.function.Predicate;

public class FixedSidedTankComponent<H extends IComponentHarness> extends SidedFluidTankComponent<H> {
    public FixedSidedTankComponent(String name, int amount, int xPos, int yPos, FacingUtil.Sideness... enabledSides) {
        super(name, amount, xPos, yPos, 0);
        this.disableFacingAddon();
        for(FacingUtil.Sideness side : FacingUtil.Sideness.values()) {
            FaceMode mode = Arrays.asList(enabledSides).contains(side) ? FaceMode.ENABLED : FaceMode.NONE;
            this.getFacingModes().put(side, mode);
        }
    }
    @Override
    public CompoundNBT serializeNBT() {
        //No need to write to NBT as they can't change at runtime. And I can't 'super super' call so this is the best I can do...
        CompoundNBT nbt = super.serializeNBT();
        nbt.remove("FacingModes");
        return nbt;
    }

    @Override
    public FixedSidedTankComponent<H> setValidator(Predicate<FluidStack> validator) {
        super.setValidator(validator);
        return this;
    }
}
