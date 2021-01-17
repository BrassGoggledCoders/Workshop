package workshop;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.codehaus.plexus.util.StringUtils;
import workshop.language.WorkshopUSLanguageProvider;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.CategoryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.EntryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import java.util.Objects;
import java.util.function.Consumer;

public class WorkshopBookProvider extends PatchouliBookProvider implements IDataProvider {
    final ExistingFileHelper helper;

    public WorkshopBookProvider(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Workshop.MOD_ID, "en_us");
        this.helper = helper;
    }
    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        final BookBuilder builder = createBookBuilder("guide", "guide.workshop.name", "guide.workshop.landing_text");
        builder.setCreativeTab(Workshop.ITEM_GROUP.getPath());
        builder.setShowProgress(false);
        builder.setVersion("1");
        builder.setModel(Workshop.MOD_ID + ":guide");
        final CategoryBuilder machineCategory =
                builder.addCategory("crafting_machine_category", "Crafting Machines", "Machines are the heart of any workshop, and these allow you to produce resources, and other machines.", new ItemStack(WorkshopBlocks.SEASONING_BARREL.getItem()));
        //addStandardEntry(machineCategory, "evaporates and then re-condenses the fumes from a mixture of things into bottles or other fluid containers. Cold items can be added to the alembic's condenser to speed it up.", WorkshopBlocks.ALEMBIC.getItem());
        //addStandardEntry(machineCategory, "is used to transform solids into liquids, often with a solid by-product. It can, for example, juice apples and squeeze oil from seeds", WorkshopBlocks.PRESS.getItem());
        addStandardEntry(machineCategory, "transforms fluids by interaction with catalyst items over an extended period of time. It cannot hold liquids hotter than lava, for that you need the $(item)Molten Reaction Chamber$()", WorkshopBlocks.SEASONING_BARREL.getItem());
        addStandardEntry(machineCategory, "allows you to heat and apply powders to other items, this can be used to coat materials. To power it, place a hot block (lava, magma block etc) one or two blocks beneath.", WorkshopBlocks.SINTERING_FURNACE.getItem());
        //addStandardEntry(machineCategory, "takes fibers and often something else and transforms it into threads, cords, or ropes.", WorkshopBlocks.SPINNING_WHEEL.getItem());
        addStandardEntry(machineCategory, "can be used for grinding things, such as metal into powder and gravel into sand", WorkshopBlocks.MORTAR.getItem());
        //addStandardEntry(machineCategory, "is similar to the $(item)Seasoning Barrel except with the ability to hold and process hot liquids.", WorkshopBlocks.MOLTEN_CHAMBER.getItem());
        addStandardEntry(machineCategory, "is for...well...drying things. It can dry items into drier versions of themselves, or evaporate off fluid to get the residue. It's pretty slow.", WorkshopBlocks.DRYING_BASIN.getItem());
        final CategoryBuilder otherMachineCategory =
                builder.addCategory("other_machine_category", "Other Machines", "More useful machines", new ItemStack(WorkshopBlocks.SCRAP_BIN.getItem()));
        EntryBuilder binEntry = addStandardEntry(otherMachineCategory, "is useful for recycling items/blocks you may have great quantities of. If more than a stack of any particular item is inserted to it, the amount over a single stack will be destroyed, and value added to the scrap bin.", WorkshopBlocks.SCRAP_BIN.getItem());
        binEntry.addSpotlightPage(new ItemStack(WorkshopItems.SCRAP_BAG.get())).setText("With enough scrap value, a scrap bin will generate a $(item)Scrap Bag$() which may be opened for an assortment of useful (or not!) stuff. However, opening the bag gives you a lingering smell that must be washed off using$(item)Soap$() before villagers will trade with you.");
        addStandardEntry(otherMachineCategory, "can be pointed at a furnace, and then when actuated by mechanical force - i.e. jumping up and down on it - will speed the furnace's smelting considerably. It can also be used to move items around.", WorkshopBlocks.BELLOWS.getItem());
        addStandardEntry(otherMachineCategory, "allows obtaining by-products from other machines that would be otherwise lost. For example, meat cooked in a furnace results in $(item)Tallow$() collected in the Collector.", WorkshopBlocks.COLLECTOR.getItem());
        final CategoryBuilder miscCategory =
                builder.addCategory("misc_category", "Miscellaneous", "Small things and features that don't fit into the other categories", new ItemStack(WorkshopBlocks.OBSIDIAN_PLATE.getItem()));
        addStandardEntry(miscCategory, "acts like a $(item)Pressure Plate$() except it will only react to players. It may be right-clicked with a name tag to blacklist that player from triggering the pressure plate. This does not consume the name tag.", WorkshopBlocks.OBSIDIAN_PLATE.getItem());
        addStandardEntry(miscCategory, "can hold up to four buckets of any liquid cooler than lava, and keeps its inventory when broken", WorkshopBlocks.SEALED_BARREL.getItem());
        addStandardEntry(miscCategory, "is a barrel with a built in hopper, so it automatically pushes items downwards", WorkshopBlocks.SILO_BARREL.getItem());
        addStandardEntry(miscCategory, "can be oriented in any direction, has one slot and attempts to output in that direction. It is like a hopper, without the pull capability.", WorkshopBlocks.ITEMDUCT.getItem());
        builder.build(consumer);
    }

    private EntryBuilder addStandardEntry(CategoryBuilder category, String text, Item item) {
        String path = Objects.requireNonNull(item.getRegistryName()).getPath();
        String name = WorkshopUSLanguageProvider.strings.getOrDefault(item.getTranslationKey(), StringUtils.capitaliseAllWords(path.replace('_', ' ')));
        final EntryBuilder entryBuilder = category.addEntry(path + "_entry", name, new ItemStack(item));
        entryBuilder.addSimpleTextPage(String.format("The $(item)%s$() ", name) + text);
        if(this.helper.exists(item.getRegistryName(), WorkshopDataGenerator.RECIPE)) {
            entryBuilder.addCraftingPage(new ResourceLocation(Workshop.MOD_ID, path));
        }
        String alt = path + "_alt";
        if(this.helper.exists(new ResourceLocation(Workshop.MOD_ID, alt), WorkshopDataGenerator.RECIPE)) {
            entryBuilder.addCraftingPage(new ResourceLocation(Workshop.MOD_ID, alt));
        }
        return entryBuilder;
    }
}
