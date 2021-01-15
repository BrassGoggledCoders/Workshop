package workshop.tags;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.tag.WorkshopBlockTags;
import xyz.brassgoggledcoders.workshop.tag.WorkshopItemTags;
import xyz.brassgoggledcoders.workshop.api.capabilities.BottleCapabilityProvider;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;

public class WorkshopItemTagsProvider extends ItemTagsProvider {

    public WorkshopItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(gen, blockTagProvider, Workshop.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerTags() {
        this.getOrCreateBuilder(WorkshopItemTags.RAW_MEAT).add(Items.RABBIT, Items.CHICKEN, Items.BEEF, Items.MUTTON, Items.PORKCHOP);
        this.getOrCreateBuilder(Tags.Items.SLIMEBALLS).add(BottleCapabilityProvider.getFilledBottle(new FluidStack(WorkshopFluids.ADHESIVE_OILS.getFluid().getFluid(), WorkshopFluids.BOTTLE_VOLUME)).getItem());
        this.getOrCreateBuilder(WorkshopItemTags.TEA_SEEDS).add(WorkshopBlocks.TEA_PLANT.getItem());
        this.getOrCreateBuilder(Tags.Items.SEEDS).add(WorkshopBlocks.TEA_PLANT.getItem());
        this.getOrCreateBuilder(WorkshopItemTags.COLD).add(Items.SNOW, Items.SNOW_BLOCK, Items.SNOWBALL, Items.ICE, Items.BLUE_ICE, Items.PACKED_ICE);
        this.copy(WorkshopBlockTags.REBARRED_CONCRETE, WorkshopItemTags.REBARRED_CONCRETE);
        this.copy(WorkshopBlockTags.STRIPPED_LOGS, WorkshopItemTags.STRIPPED_LOGS);
        this.getOrCreateBuilder(WorkshopItemTags.TALLOW).add(WorkshopItems.TALLOW.get());
        this.getOrCreateBuilder(WorkshopItemTags.ROOTS).add(Items.POISONOUS_POTATO).addOptional(new ResourceLocation("quark", "root_item"));
        this.getOrCreateBuilder(WorkshopItemTags.SALT).add(WorkshopItems.SALT.get());
        this.getOrCreateBuilder(ItemTags.makeWrapperTag("forge:salt")).add(WorkshopItems.SALT.get());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Item Tags";
    }
}
