package workshop.language;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fluids.FluidStack;
import org.codehaus.plexus.util.StringUtils;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.api.capabilities.BottleCapabilityProvider;
import xyz.brassgoggledcoders.workshop.content.*;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import java.util.Map;
import java.util.TreeMap;

public class WorkshopUSLanguageProvider extends LanguageProvider {

    //Publicised for use in BookProvider
    public static final Map<String, String> strings = new TreeMap<>();

    public WorkshopUSLanguageProvider(DataGenerator gen) {
        super(gen, Workshop.MOD_ID, "en_us");
    }

    protected WorkshopUSLanguageProvider(DataGenerator gen, String locale) {
        super(gen, Workshop.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        //region Blocks
        this.addBlock(WorkshopBlocks.ALEMBIC, "Alembic");
        this.addBlock(WorkshopBlocks.BROKEN_ANVIL, "Broken Anvil");
        this.addBlock(WorkshopBlocks.OBSIDIAN_PLATE, "Obsidian Plate");
        this.addBlock(WorkshopBlocks.PRESS, "Press");
        this.addBlock(WorkshopBlocks.SEASONING_BARREL, "Seasoning Barrel");
        this.addBlock(WorkshopBlocks.MOLTEN_CHAMBER, "Molten Reaction Chamber");
        this.addBlock(WorkshopBlocks.SINTERING_FURNACE, "Sintering Furnace");
        this.addBlock(WorkshopBlocks.SPINNING_WHEEL, "Spinning Wheel");
        int i = 0;
        for (BlockRegistryObjectGroup<?, ?, ?> concrete : WorkshopBlocks.CONCRETES) {
            this.addBlock(concrete, String.format("%s Rebarred Concrete", StringUtils.capitaliseAllWords(DyeColor.values()[i++].name().toLowerCase().replace("_", " "))));
        }
        this.addBlock(WorkshopBlocks.TEA_PLANT, "Tea");
        this.addBlock(WorkshopBlocks.SEALED_BARREL, "Sealed Barrel");
        this.addBlock(WorkshopBlocks.BELLOWS, "Bellows");
        this.addBlock(WorkshopBlocks.COLLECTOR, "Byproduct Collector");
        this.addBlock(WorkshopBlocks.SCRAP_BIN, "Scrap Bin");
        this.addBlock(WorkshopBlocks.MORTAR, "Mortar and Pestle");
        this.addBlock(WorkshopBlocks.STRIPPED_SEASONED_LOG, "Stripped Seasoned Log");
        this.addBlock(WorkshopBlocks.SEASONED_LOG, "Seasoned Log");
        this.addBlock(WorkshopBlocks.DRYING_BASIN, "Drying Basin");
        this.addBlock(WorkshopBlocks.FLUID_FUNNEL, "Fluid Funnel");
        this.addBlock(WorkshopBlocks.SILO_BARREL, "Silo Barrel");
        this.addBlock(WorkshopBlocks.ITEMDUCT, "Itemduct");
        this.addBlock(WorkshopBlocks.SILT, "Silt");
        this.addBlock(WorkshopBlocks.SILTSTONE, "Siltstone");
        this.addBlock(WorkshopBlocks.BOTTLE_RACK, "Bottling Rack");
        //endregion

        //region Items
        this.add("itemGroup.workshop", "Workshop");
        this.addItem(WorkshopItems.CARAMEL_APPLE, "Caramel Apple");
        this.addItem(WorkshopItems.SALT, "Salt");
        this.addItem(WorkshopItems.ASH, "Ash");
        this.addItem(WorkshopItems.ROSIN, "Rosin");
        this.addItem(WorkshopItems.PICKLE, "Pickle");
        this.addItem(WorkshopBlocks.TEA_PLANT::getItem, "Tea Seeds");
        this.addItem(WorkshopItems.TEA_LEAVES, "Tea Leaves");
        this.addItem(WorkshopItems.TANNIN, "Tannin");
        this.addItem(WorkshopItems.TALLOW, "Tallow");
        this.addItem(WorkshopItems.SOAP, "Soap");
        this.addItem(WorkshopItems.SCRAP_BAG, "Scrap Bag");
        //this.addItem(WorkshopItems.SILT, "Silt");
        this.addItem(WorkshopItems.LEATHER_CORDAGE, "Leather Cordage");
        this.addItem(WorkshopItems.LYE, "Lye");
        //endregion

        //region Fluids
        this.addFluid(WorkshopFluids.BRINE, "Brine");
        this.addFluid(WorkshopFluids.DISTILLED_WATER, "Distilled Water");
        this.addFluid(WorkshopFluids.SEED_OIL, "Seed Oil");
        this.addFluid(WorkshopFluids.APPLE_JUICE, "Apple Juice");
        this.addFluid(WorkshopFluids.CIDER, "Cider");
        this.addFluid(WorkshopFluids.HELLBLOOD, "Hellblood");
        this.addFluid(WorkshopFluids.RESIN, "Resin");
        this.addFluid(WorkshopFluids.ADHESIVE_OILS, "Adhesive Oils");
        //this.addFluid(WorkshopFluids.CHERRY_JUICE, "Cherry Juice");
        this.addFluid(WorkshopFluids.GLACIAL_WATER, "Glacial Water");
        this.addFluid(WorkshopFluids.TEA, "Tea");
        this.addFluid(WorkshopFluids.HONEY, "Honey");
        this.addFluid(WorkshopFluids.MEAD, "Mead");
        this.addFluid(WorkshopFluids.POTASH_WATER, "Potash Water");
        //endregion

        //region Guide
        this.add("guide.workshop.name", "Workshop Guide");
        this.add("guide.workshop.landing_text", "A pocket handbook to the world of small scale manufacturing");
        //endregion

        //region Tooltips
        this.addFacingTooltip(InventoryUtil.ITEM_INPUT, "Item Input");
        this.addFacingTooltip(InventoryUtil.ITEM_OUTPUT, "Item Output");
        this.addFacingTooltip(InventoryUtil.FLUID_INPUT, "Fluid Input");
        this.addFacingTooltip(InventoryUtil.FLUID_OUTPUT, "Fluid Output");
        this.addFacingTooltip("residue", "Residue");
        this.addFacingTooltip("colditem", "Cooling Source");
        this.addFacingTooltip("container", "Container");
        this.addFacingTooltip("powderinventory", "Powder");
        this.addFacingTooltip("fuelinventory", "Fuel");
        this.addFacingTooltip("output", "Output");
        //endregion

        //region Resources
        this.add("itemGroup.resources", "Titanium Resources");
        this.add("resource.titanium.material.iron", "Iron");
        this.add("resource.titanium.material.gold", "Gold");
        this.add("resource.titanium.material.glass", "Glass");
        this.add("resource.titanium.material.copper", "Copper");
        this.add("resource.titanium.material.silver", "Silver");
        this.addResource("dust", "%s Powder");
        this.addResource("film", "%s Film");
        this.addResource("pipe", "%s Pipe");
        this.addResource("nugget", "%s Nugget");
        this.addResource("ingot", "%s Ingot");
        this.addResource("metal_block", "Block of %s");
        //endregion

        //region Effects
        this.addEffect(WorkshopEffects.STINKY, "Stinky");
        this.addEffect(WorkshopEffects.INEBRIATED, "Inebriated");
        this.addEffect(WorkshopEffects.DRUNK, "Drunk");
        this.addEffect(WorkshopEffects.MELLOW, "Mellow");
        this.addEffect(WorkshopEffects.RUSH, "Caffeine Rush");
        this.addEffect(WorkshopEffects.WIRED, "Wired");
        //endregion

        //region JEI
        this.add(Workshop.SCRAP_BAG_DESC, "Scrap bags are generated when a Scrap Bin destroys enough excess items (more than a stack of any one item). They can be opened by hand or in a dispenser for some items.");
        this.add(Workshop.FLUID_FUNNEL_DESC, "Like a Hopper, but for Fluids");
        //endregion

        this.addAdvancement("root", "A wright's beginning...", "Measure twice. Cut once.");
        this.addAdvancement("bellows", "Embers' friend", "Craft Bellows");
        this.addAdvancement("drying_basin", "Hurry up and wait", "Craft the Drying Basin");
        this.addAdvancement("scrap_bin", "Recycling!", "Craft the Scrap Bin");
        this.addAdvancement("mortar", "crushcrushcrush", "Craft the Mortal & Pestle");
        this.addAdvancement("seasoning_barrel", "Aquatic Chemistry M*gic", "Craft the Seasoning Barrel");
        this.addAdvancement("drunk", "Get Drunk", "*hick*");
    }

    public void addFluid(FluidRegistryObjectGroup<?, ?> fluid, String name) {
        this.add("fluid.workshop." + fluid.getName(), name);
        this.addBlock(fluid::getBlock, name);
        this.addItem(fluid::getBucket, String.format("Bucket of %s", name));
        this.addItem(() -> BottleCapabilityProvider.getFilledBottle(new FluidStack(fluid.getFluid(), 1)).getItem(), String.format("Bottle of %s", name));
    }

    public void addFacingTooltip(String key, String name) {
        String prefix = "tooltip.titanium.facing_handler.";
        this.add(prefix + key, name);
    }

    public void addResource(String key, String name) {
        String prefix = "resource.titanium.type.";
        this.add(prefix + key, name);
    }

    public void addAdvancement(String key, String name, String desc) {
        this.add("advancement.workshop." + key, name);
        this.add("advancement.workshop." + key + ".desc", desc);
    }

    @Override
    public void add(String key, String value) {
        strings.put(key, value);
        super.add(key, value);
    }
}
