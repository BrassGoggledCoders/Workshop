package xyz.brassgoggledcoders.workshop.blocks;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

public abstract class WorkshopGUIMachine<T extends ActiveTile<T>> extends ActiveTile<T> {
    @Save
    private ProgressBarComponent progressBar;

    public WorkshopGUIMachine(BasicTileBlock base, int x, int y, int maxprogress, ProgressBarComponent.BarDirection direction) {
        super(base);
        this.addProgressBar(progressBar = new ProgressBarComponent(x, y, maxprogress)
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
