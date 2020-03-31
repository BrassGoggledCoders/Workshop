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
        this.addBlock(WorkshopFluids.BRINE::getBlock, "Brine");
        this.addItem(WorkshopFluids.BRINE::getBucket, "Bucket of Brine");
        this.addBlock(WorkshopFluids.DISTILLED_WATER::getBlock, "Distilled Water");
        this.addItem(WorkshopFluids.DISTILLED_WATER::getBucket, "Bucket of Distilled Water");
        this.addBlock(WorkshopFluids.SEED_OIL::getBlock, "Seed Oil");
        this.addItem(WorkshopFluids.SEED_OIL::getBucket, "Bucket of Seed Oil");
        //endregion

        //region Guide
        this.add("guide.workshop.name", "Workshop Guide");
        this.add("guide.workshop.landing_text", "A pocket handbook to the world of small scale manufacturing");
        //endregion

        //region Tooltips
        String prefix = "tooltip.titanium.facing_handler.";
        this.add(prefix + "input", "Input");
        this.add(prefix + "output", "Output");
        this.add(prefix + "residue", "Residue");
        this.add(prefix + "colditem","Cooling Source");
        this.add(prefix + "container", "Container");
        //endregion
    }
}
