package xyz.brassgoggledcoders.workshop;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class WorkshopBlockTags {
    public static final ITag.INamedTag<Block> REBARRED_CONCRETE = BlockTags.makeWrapperTag(Workshop.MOD_ID + ":rebarred_concrete");
    public static final ITag.INamedTag<Block> HOT = BlockTags.makeWrapperTag(Workshop.MOD_ID + ":hot"); //Includes VERY_HOT
    public static final ITag.INamedTag<Block> VERY_HOT = BlockTags.makeWrapperTag(Workshop.MOD_ID + ":very_hot");
}
