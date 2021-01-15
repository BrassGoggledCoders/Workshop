package xyz.brassgoggledcoders.workshop.tag;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import xyz.brassgoggledcoders.workshop.Workshop;

public class WorkshopBlockTags {
    public static final ITag.INamedTag<Block> REBARRED_CONCRETE = BlockTags.makeWrapperTag(Workshop.MOD_ID + ":rebarred_concrete");
    public static final ITag.INamedTag<Block> HOT = BlockTags.makeWrapperTag(Workshop.MOD_ID + ":hot"); //Includes VERY_HOT
    public static final ITag.INamedTag<Block> VERY_HOT = BlockTags.makeWrapperTag(Workshop.MOD_ID + ":very_hot");
    public static final ITag.INamedTag<Block> STRIPPED_LOGS = BlockTags.makeWrapperTag("forge:stripped_logs");
}
