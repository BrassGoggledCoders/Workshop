package xyz.brassgoggledcoders.workshop.component.machine;

import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.nbt.CompoundNBT;

import java.util.Arrays;

public class FixedSidedInventoryComponent<H extends IComponentHarness> extends SidedInventoryComponent<H> {
    public static final FacingUtil.Sideness[] NOT_BOTTOM = except(FacingUtil.Sideness.BOTTOM);

    public FixedSidedInventoryComponent(String name, int xPos, int yPos, int size, FacingUtil.Sideness... enabledSides) {
        super(name, xPos, yPos, size, 0);
        this.disableFacingAddon();
        for(FacingUtil.Sideness side : FacingUtil.Sideness.values()) {
            FaceMode mode = Arrays.asList(enabledSides).contains(side) ? FaceMode.ENABLED : FaceMode.NONE;
            this.getFacingModes().put(side, mode);
        }
    }

    public static FacingUtil.Sideness[] except(FacingUtil.Sideness side) {
        return Arrays.stream(FacingUtil.Sideness.values()).filter(s -> s != side).toArray(FacingUtil.Sideness[]::new);
    }

    @Override
    public CompoundNBT serializeNBT() {
        //No need to write to NBT as they can't change at runtime. And I can't 'super super' call so this is the best I can do...
        CompoundNBT nbt = super.serializeNBT();
        nbt.remove("FacingModes");
        return nbt;
    }
}
