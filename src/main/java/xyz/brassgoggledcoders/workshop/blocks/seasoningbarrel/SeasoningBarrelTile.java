package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;


import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SEASONING_BARREL_BLOCK;

public class SeasoningBarrelTile extends WorkshopGUIMachine {


    public SeasoningBarrelTile() {
        super(SEASONING_BARREL_BLOCK, 102, 42, 100,  PosProgressBar.BarDirection.HORIZONTAL_RIGHT);

    }

    @Override
    public Runnable onFinish() {
        return null;
    }
}
