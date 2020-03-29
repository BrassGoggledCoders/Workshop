package xyz.brassgoggledcoders.workshop.datagen.langauge;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

public class WorkshopUSLanguageProvider extends LanguageProvider {
    public WorkshopUSLanguageProvider(DataGenerator gen) {
        super(gen, Workshop.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //region Blocks
        this.add(WorkshopBlocks.ALEMBIC.getBlock(), "Alembic");
        this.add(WorkshopBlocks.BROKEN_ANVIL.getBlock(), "Broken Anvil");
        this.add(WorkshopBlocks.OBSIDIAN_PLATE.getBlock(), "Obsidian Plate");
        this.add(WorkshopBlocks.PRESS.getBlock(), "Press");
        this.add(WorkshopBlocks.SEASONING_BARREL.getBlock(), "Seasoning Barrel");
        this.add(WorkshopBlocks.SINTERING_FURNACE.getBlock(), "Sintering Furnace");
        this.add(WorkshopBlocks.SPINNING_WHEEL.getBlock(), "Spinning Wheel");
        //endregion

        //region Items
        this.add("itemGroup.workshop", "Workshop");
        this.addItem(WorkshopItems.CARAMEL_APPLE, "Caramel Apple");
        this.addItem(WorkshopItems.SALT, "Salt");
        //endregion

        //region Fluids
        this.addBlock(WorkshopFluids.BRINE_BLOCK, "Brine");
        this.addItem(WorkshopFluids.BRINE_BUCKET, "Bucket of Brine");
        //endregion

        //region Guide
        this.add("guide.workshop.name", "Workshop Guide");
        this.add("guide.workshop.landing_text", "A pocket handbook to the world of small scale manufacturing");
        //endregion
    }
}
