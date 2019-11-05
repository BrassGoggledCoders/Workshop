package xyz.brassgoggledcoders.workshop.blocks.press;

import xyz.brassgoggledcoders.workshop.blocks.WorkShopMachine;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.PRESS_BLOCK;

public class PressTile extends WorkShopMachine {

    public PressTile() {
        super(PRESS_BLOCK,102, 42);
    }

    @Override
    public Runnable onFinish() {
        return null;
    }



}
