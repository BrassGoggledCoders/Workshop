package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class WorkshopTags {

    public static class Items {
        public static final Tag<Item> COLD = tag("cold");
        public static final Tag<Item> FUEL = tag("fuel");
        public static final Tag<Item> POWDER = tag("powder");
        public static final Tag<Item> PRESS = tag("press");

        private static Tag<Item> tag(String name) {
            return new ItemTags.Wrapper(new ResourceLocation(MOD_ID, name));
        }
    }

    public static class Blocks {

        private static Tag<Block> tag(String name) {
            return new BlockTags.Wrapper(new ResourceLocation(MOD_ID, name));
        }
    }

}
