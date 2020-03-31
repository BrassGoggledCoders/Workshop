package xyz.brassgoggledcoders.workshop.datagen.langauge;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.FluidRegistryObjectGroup;
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
        this.addFluid(WorkshopFluids.BRINE, "Brine");
        this.addFluid(WorkshopFluids.DISTILLED_WATER, "Distilled Water");
        this.addFluid(WorkshopFluids.SEED_OIL, "Seed Oil");
        this.addFluid(WorkshopFluids.APPLE_JUICE, "Apple Juice");
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

    public void addFluid(FluidRegistryObjectGroup fluid, String name) {
        this.addBlock(fluid::getBlock, name);
        this.addItem(fluid::getBucket, String.format("Bucket of %s", name));
    }
}
