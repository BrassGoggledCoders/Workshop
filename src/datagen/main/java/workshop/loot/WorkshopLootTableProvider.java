package workshop.loot;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WorkshopLootTableProvider extends LootTableProvider {
    public WorkshopLootTableProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Loot Tables";
    }

    @Override
    @Nonnull
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return Lists.newArrayList(Pair.of(WorkshopBlockLootTables::new, LootParameterSets.BLOCK), Pair.of(WorkshopGiftLootTables::new, LootParameterSets.GIFT));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
    }
}
