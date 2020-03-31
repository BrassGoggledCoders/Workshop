package xyz.brassgoggledcoders.workshop.datagen.langauge;

import com.hrznstudio.titanium.util.StringUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.*;

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
        int i = 0;
        for(BlockRegistryObjectGroup concrete : WorkshopBlocks.CONCRETES) {
            //TODO Dye names, plus grey vs gray
            this.add(concrete.getBlock(), String.format("%s Rebarred Concrete", StringUtils.capitalise(DyeColor.values()[i++].getName().replace("_", " "))));
        }
        //endregion

        //region Items
        this.add("itemGroup.workshop", "Workshop");
        this.addItem(WorkshopItems.CARAMEL_APPLE, "Caramel Apple");
        this.addItem(WorkshopItems.SALT, "Salt");
        this.addItem(WorkshopItems.ASH, "Ash");
        this.addItem(WorkshopItems.ROSIN, "Rosin");
        this.addItem(WorkshopItems.PICKLE, "Pickle");
        //endregion

        //region Fluids
        this.addFluid(WorkshopFluids.BRINE, "Brine");
        this.addFluid(WorkshopFluids.DISTILLED_WATER, "Distilled Water");
        this.addFluid(WorkshopFluids.SEED_OIL, "Seed Oil");
        this.addFluid(WorkshopFluids.APPLE_JUICE, "Apple Juice");
        this.addFluid(WorkshopFluids.CIDER, "Cider");
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
