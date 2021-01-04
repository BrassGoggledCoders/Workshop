package workshop.loot;

import net.minecraft.data.loot.GiftLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.workshop.Workshop;

import java.util.function.BiConsumer;


public class WorkshopGiftLootTables extends GiftLootTables {
    public static final ResourceLocation SCRAP_BAG = new ResourceLocation(Workshop.MOD_ID, "gameplay/scrap_bag");

    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(SCRAP_BAG, LootTable.builder()
                .addLootPool(LootPool.builder().rolls(RandomValueRange.of(1, 3))
                        .addEntry(ItemLootEntry.builder(Items.REDSTONE).weight(5))
                        .addEntry(ItemLootEntry.builder(Items.SPIDER_EYE).weight(26))
                        .addEntry(ItemLootEntry.builder(Items.STRING).weight(20))
                        .addEntry(ItemLootEntry.builder(Items.PAPER).weight(20))
                        .addEntry(ItemLootEntry.builder(Items.GLASS_BOTTLE).weight(10))
                        .addEntry(TagLootEntry.getBuilder(Tags.Items.NUGGETS).weight(4))
                        .addEntry(TagLootEntry.getBuilder(Tags.Items.SEEDS).weight(10))
                ));
    }
}
