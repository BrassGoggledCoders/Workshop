package xyz.brassgoggledcoders.workshop.blocks;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import javax.annotation.Nonnull;

public abstract class WorkshopGUIMachine<T extends ActiveTile<T>> extends ActiveTile<T> {
    private ProgressBarComponent progressBar;

    public WorkshopGUIMachine(BasicTileBlock base, int x, int y, int maxProgress, ProgressBarComponent.BarDirection direction) {
        super(base);
        this.addProgressBar(progressBar = new ProgressBarComponent(x, y, maxProgress)
                .setBarDirection(direction)
                .setCanReset(tileEntity -> true)
                .setOnStart(() -> progressBar.setMaxProgress(getMaxProgress()))
                .setCanIncrease(tileEntity -> canIncrease())
                .setOnFinishWork(() -> onFinish().run()));
    }

    public ProgressBarComponent getProgressBar() {
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

    @Override
    public ActionResultType onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY,
                                        double hitZ) {
        openGui(playerIn);
        return ActionResultType.SUCCESS;
    }

    public abstract boolean canIncrease();

    public int getMaxProgress() {
        return 100;
    }

    public abstract Runnable onFinish();
}
