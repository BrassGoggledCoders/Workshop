package xyz.brassgoggledcoders.workshop.datagen.loot;

import net.minecraft.data.loot.GiftLootTables;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import xyz.brassgoggledcoders.workshop.Workshop;

import java.util.function.BiConsumer;

public class WorkshopGiftLootTables extends GiftLootTables {
    public static final ResourceLocation SCRAP_BAG = new ResourceLocation(Workshop.MOD_ID, "gameplay/scrap_bag");

    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(SCRAP_BAG, LootTable.builder()
                .addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.DIAMOND).weight(1)).addEntry(ItemLootEntry.builder(Items.ARROW).weight(26))));
    }
}
