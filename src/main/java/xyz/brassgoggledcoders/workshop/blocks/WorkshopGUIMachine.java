package xyz.brassgoggledcoders.workshop.blocks;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

public abstract class WorkshopGUIMachine extends TileActive {

    @Save
    private PosProgressBar progressBar;

    public WorkshopGUIMachine(BlockTileBase base, int x, int y, int maxprogress, PosProgressBar.BarDirection direction) {
        super(base);
        this.addProgressBar(progressBar = new PosProgressBar(x, y, maxprogress).
                setTile(this).
                setBarDirection(direction).
                setCanReset(tileEntity -> true).
                setOnStart(() -> progressBar.setMaxProgress(getMaxProgress())).
                setOnFinishWork(() -> onFinish().run())
        );
    }

    public PosProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ)) return true;
        openGui(playerIn);
        return true;
    }

    public int getMaxProgress() {
        return 100;
    }

    public abstract Runnable onFinish();

}
