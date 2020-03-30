package xyz.brassgoggledcoders.workshop.datagen.loot;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;

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
        this.registerLootTable(WorkshopBlocks.ALEMBIC.getBlock(), BlockLootTables::droppingWithName);
        this.registerDropSelfLootTable(WorkshopBlocks.BROKEN_ANVIL.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.OBSIDIAN_PLATE.getBlock());
        this.registerLootTable(WorkshopBlocks.PRESS.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SEASONING_BARREL.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SINTERING_FURNACE.getBlock(), BlockLootTables::droppingWithName);
        this.registerLootTable(WorkshopBlocks.SPINNING_WHEEL.getBlock(), BlockLootTables::droppingWithName);
    }
}
