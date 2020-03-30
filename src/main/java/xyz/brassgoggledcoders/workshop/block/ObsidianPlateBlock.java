package xyz.brassgoggledcoders.workshop.block;

import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;

public class ObsidianPlateBlock extends PressurePlateBlock {
    public ObsidianPlateBlock() {
        super(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.create(Material.ROCK));
    }
}
