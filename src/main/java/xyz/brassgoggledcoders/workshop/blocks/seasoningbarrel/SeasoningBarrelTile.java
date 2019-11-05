package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;


import xyz.brassgoggledcoders.workshop.blocks.WorkShopMachine;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SEASONING_BARREL_BLOCK;

public class SeasoningBarrelTile extends WorkShopMachine {


    public SeasoningBarrelTile() {
        super(SEASONING_BARREL_BLOCK, 102, 42);

    }

    @Override
    public Runnable onFinish() {
        return null;
    }
}
