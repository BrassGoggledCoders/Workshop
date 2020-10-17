package xyz.brassgoggledcoders.workshop.datagen.loot;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.functions.CopyNbt;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.content.WorkshopResourcePlugin;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class WorkshopBlockLootTables extends BlockLootTables {
    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        final List<Block> workshopBlocks = WorkshopBlocks.getAllBlocks()
                .stream()
                .map(RegistryObject::get)
                .collect(Collectors.toList());
        workshopBlocks.add(WorkshopResourcePlugin.COPPER_BLOCK);
        workshopBlocks.add(WorkshopResourcePlugin.SILVER_BLOCK);
        return workshopBlocks;
    }

    @Override
    protected void addTables() {
        StreamSupport.stream(this.getKnownBlocks().spliterator(), false)
                .filter(block -> block.getRegistryName().getPath()
                        .contains("concrete")).forEach(this::registerDropSelfLootTable);
        this.registerDropSelfLootTable(WorkshopBlocks.BROKEN_ANVIL.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.OBSIDIAN_PLATE.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.BELLOWS.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.SEASONED_LOG.getBlock());
        this.registerDropSelfLootTable(WorkshopResourcePlugin.COPPER_BLOCK.getBlock());
        this.registerDropSelfLootTable(WorkshopResourcePlugin.SILVER_BLOCK.getBlock());

        this.registerLootTable(WorkshopBlocks.TEA_PLANT.getBlock(), droppingAndBonusWhen(WorkshopBlocks.TEA_PLANT.getBlock(), WorkshopItems.TEA_LEAVES.get(), WorkshopBlocks.TEA_PLANT.getItem(),
                BlockStateProperty.builder(WorkshopBlocks.TEA_PLANT.getBlock()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(CropsBlock.AGE, 1))));

        //region Machines
        this.registerLootTable(WorkshopBlocks.ALEMBIC.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.PRESS.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SEASONING_BARREL.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.MOLTEN_CHAMBER.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SINTERING_FURNACE.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SPINNING_WHEEL.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.COLLECTOR.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SCRAP_BIN.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.MORTAR.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.DRYING_BASIN.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.FLUID_FUNNEL.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SILO_BARREL.getBlock(), BlockLootTables::droppingWithName);
        //endregion

        this.registerLootTable(WorkshopBlocks.PRESS_ARM.getBlock(), new LootTable.Builder());
        this.registerLootTable(WorkshopBlocks.PRESS_TOP.getBlock(), new LootTable.Builder());
        this.registerLootTable(WorkshopBlocks.CHALK_WRITING.getBlock(), new LootTable.Builder());

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
