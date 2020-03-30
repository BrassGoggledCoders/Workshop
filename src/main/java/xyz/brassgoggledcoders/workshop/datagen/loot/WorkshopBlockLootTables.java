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
                ).collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        this.registerDropSelfLootTable(WorkshopBlocks.ALEMBIC.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.BROKEN_ANVIL.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.OBSIDIAN_PLATE.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.PRESS.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.SEASONING_BARREL.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.SINTERING_FURNACE.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.SPINNING_WHEEL.getBlock());
    }
}
