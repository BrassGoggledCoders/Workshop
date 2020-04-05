package xyz.brassgoggledcoders.workshop.datagen.loot;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.CopyNbt;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WorkshopBlockLootTables extends BlockLootTables {
    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS
                .getValues()
                .stream()
                .filter(block -> Optional.ofNullable(block.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(Workshop.MOD_ID))
                        .isPresent()
                )
                .collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        StreamSupport.stream(this.getKnownBlocks().spliterator(), false)
                .filter(block -> block.getRegistryName().getPath()
                        .contains("concrete")).forEach(block -> registerDropSelfLootTable(block));

        this.registerDropSelfLootTable(WorkshopBlocks.BROKEN_ANVIL.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.OBSIDIAN_PLATE.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.BELLOWS.getBlock());

        this.registerLootTable(WorkshopBlocks.TEA_PLANT.getBlock(), droppingAndBonusWhen(WorkshopBlocks.TEA_PLANT.getBlock(), WorkshopItems.TEA_LEAVES.get(), WorkshopBlocks.TEA_PLANT.getItem(),
                BlockStateProperty.builder(WorkshopBlocks.TEA_PLANT.getBlock()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(CropsBlock.AGE, 1))));

        this.registerLootTable(WorkshopBlocks.ALEMBIC.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.PRESS.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SEASONING_BARREL.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SINTERING_FURNACE.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SPINNING_WHEEL.getBlock(), BlockLootTables::droppingWithName);

        this.registerLootTable(WorkshopBlocks.SEALED_BARREL.getBlock(), new LootTable.Builder()
                .addLootPool(LootPool.builder()
                        .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                .replaceOperation("capability", "BlockEntityTag.capability"))
                        .rolls(RandomValueRange.of(1, 1))
                        .addEntry(ItemLootEntry.builder(WorkshopBlocks.SEALED_BARREL.getBlock()))
                )
        );
    }
}
