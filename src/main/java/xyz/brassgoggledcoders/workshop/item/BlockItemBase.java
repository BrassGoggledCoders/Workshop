package xyz.brassgoggledcoders.workshop.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class BlockItemBase extends BlockItem {
    public BlockItemBase(Block block, Properties properties, String id) {
        super(block, properties);
        setRegistryName(id);
    }
}
