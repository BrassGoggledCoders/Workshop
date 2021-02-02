package workshop.loot;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraftforge.fml.RegistryObject;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.GUITileBlock;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class WorkshopBlockLootTables extends BlockLootTables {
    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return WorkshopBlocks.getAllBlocks()
                .stream()
                .map(RegistryObject::get)
                .collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        for(RegistryObject<Block> block : WorkshopBlocks.getAllBlocks()) {
            block.ifPresent(actualBlock -> {
                if(actualBlock.getRegistryName().getPath().contains("concrete")) {
                    registerDropSelfLootTable(actualBlock);
                }
                if(actualBlock instanceof GUITileBlock<?>) {
                    registerLootTable(actualBlock, BlockLootTables::droppingWithName);
                }
            });
        }
        this.registerDropSelfLootTable(WorkshopBlocks.BROKEN_ANVIL.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.OBSIDIAN_PLATE.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.BELLOWS.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.SEASONED_LOG.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.STRIPPED_SEASONED_LOG.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.ITEMDUCT.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.SILT.getBlock());
        this.registerDropSelfLootTable(WorkshopBlocks.SILTSTONE.getBlock());

        this.registerLootTable(WorkshopBlocks.TEA_PLANT.getBlock(), droppingAndBonusWhen(WorkshopBlocks.TEA_PLANT.getBlock(), WorkshopItems.TEA_LEAVES.get(), WorkshopBlocks.TEA_PLANT.getItem(),
                BlockStateProperty.builder(WorkshopBlocks.TEA_PLANT.getBlock()).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(CropsBlock.AGE, 1))));

        this.registerLootTable(WorkshopBlocks.PRESS_TOP.getBlock(), new LootTable.Builder());

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
