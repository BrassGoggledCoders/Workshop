package xyz.brassgoggledcoders.workshop.blocks;

import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.components.MachineComponent;

import javax.annotation.Nonnull;

public abstract class WorkshopGUIMachine<T extends WorkshopGUIMachine<T>> extends TileEntity implements IComponentHarness, ITickableTileEntity {
    private final MachineComponent<T> machineComponent;
    private ProgressBarComponent<T> progressBar;

    @SuppressWarnings("unchecked")
    public WorkshopGUIMachine(TileEntityType<T> tTileEntityType, ProgressBarComponent<T> progressBar) {
        super(tTileEntityType);
        this.machineComponent = new MachineComponent<>((T) this, this::getPos);
        this.machineComponent.addProgressBar(this.progressBar = progressBar
                .setOnStart(() -> progressBar.setMaxProgress(getMaxProgress()))
                .setCanIncrease(tileEntity -> canIncrease())
                .setOnFinishWork(this::onFinish));
    }

    public MachineComponent<T> getMachineComponent() {
        return this.machineComponent;
    }

    public ProgressBarComponent<T> getProgressBar() {
        return progressBar;
    }

    @Override
    public void read(CompoundNBT compound) {
        progressBar.deserializeNBT(compound.getCompound("progress"));
        super.read(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("progress", progressBar.serializeNBT());
        return super.write(compound);
    }

    public ActionResultType onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY,
                                        double hitZ) {
        return ActionResultType.SUCCESS;
    }

    public abstract boolean canIncrease();

    public int getMaxProgress() {
        return 100;
    }

    public abstract void onFinish();

    public World getComponentWorld() {
        return this.world;
    }

    public void markComponentForUpdate() {

    }

    public void markComponentDirty() {
        this.markDirty();
    }
}
