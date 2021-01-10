package xyz.brassgoggledcoders.workshop;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class WorkshopItemTags {
    public static final ITag.INamedTag<Item> TEA_SEEDS = ItemTags.makeWrapperTag("forge:seeds/tea");
    public static final ITag.INamedTag<Item> RAW_MEAT = ItemTags.makeWrapperTag("forge:raw_meats");
    public static final ITag.INamedTag<Item> COLD = ItemTags.makeWrapperTag(Workshop.MOD_ID + ":cold");
    public static final ITag.INamedTag<Item> REBARRED_CONCRETE = ItemTags.makeWrapperTag(Workshop.MOD_ID + ":rebarred_concrete");
    public static final ITag.INamedTag<Item> ROOTS = ItemTags.makeWrapperTag(Workshop.MOD_ID + ":roots");
    public static final ITag.INamedTag<Item> TALLOW = ItemTags.makeWrapperTag("forge:tallow");
    public static final ITag.INamedTag<Item> SALT = ItemTags.makeWrapperTag("forge:dusts/salt");
    //Filled automatically by Titanium
    public static final ITag.INamedTag<Item> IRON_FILM = ItemTags.makeWrapperTag("forge:films/iron");
    public static final ITag.INamedTag<Item> STRIPPED_LOGS = ItemTags.makeWrapperTag("forge:stripped_logs");
}
