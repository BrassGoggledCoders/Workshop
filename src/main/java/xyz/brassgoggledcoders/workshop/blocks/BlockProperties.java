package xyz.brassgoggledcoders.workshop.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockProperties
{


    public static Block.Properties METAL_BLOCK = builder(Material.IRON, null, SoundType.METAL, 5.0f);



    private static Block.Properties builder(Material material, @Nullable ToolType toolType, SoundType soundType, float hardnessnresist) {

        return Block.Properties.create(material).hardnessAndResistance(hardnessnresist).harvestTool(toolType).sound(soundType);
    }

}
