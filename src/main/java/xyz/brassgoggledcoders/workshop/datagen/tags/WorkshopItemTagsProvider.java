package xyz.brassgoggledcoders.workshop.datagen.tags;


import com.hrznstudio.titanium.material.ResourceRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.capabilities.BottleCapabilityProvider;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

public class WorkshopItemTagsProvider extends ItemTagsProvider {

    public static final Tag<Item> TEA_SEEDS = new ItemTags.Wrapper(new ResourceLocation("forge", "seeds/tea"));
    public static final Tag<Item> RAW_MEAT = new ItemTags.Wrapper(new ResourceLocation("forge", "raw_meats"));
    public static final Tag<Item> COLD = new ItemTags.Wrapper(new ResourceLocation(Workshop.MOD_ID, "cold"));
    public static final Tag<Item> REBARRED_CONCRETE = new ItemTags.Wrapper(new ResourceLocation(Workshop.MOD_ID, "rebarred_concrete"));
    public static final Tag<Item> ROOTS = new ItemTags.Wrapper(new ResourceLocation(Workshop.MOD_ID, "roots"));
    public static  final Tag<Item> TALLOW = new ItemTags.Wrapper(new ResourceLocation("forge", "tallow"));

    public WorkshopItemTagsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(RAW_MEAT).add(Items.RABBIT, Items.CHICKEN, Items.BEEF, Items.MUTTON, Items.PORKCHOP);
        this.getBuilder(Tags.Items.SLIMEBALLS).add(BottleCapabilityProvider.getFilledBottle(new FluidStack(WorkshopFluids.ADHESIVE_OILS.getFluid().getFluid(), WorkshopFluids.BOTTLE_VOLUME)).getItem());
        this.getBuilder(TEA_SEEDS).add(WorkshopBlocks.TEA_PLANT.getItem());
        this.getBuilder(Tags.Items.SEEDS).add(WorkshopBlocks.TEA_PLANT.getItem());
        this.getBuilder(COLD).add(Items.SNOW, Items.SNOW_BLOCK, Items.SNOWBALL, Items.ICE, Items.BLUE_ICE, Items.PACKED_ICE);
        WorkshopBlocks.CONCRETES.forEach(c -> this.getBuilder(REBARRED_CONCRETE).add(c.getItem()));
        this.getBuilder(TALLOW).add(WorkshopItems.TALLOW.get());
        this.getBuilder(ROOTS).add(Items.POISONOUS_POTATO).addOptional(ItemTags.getCollection(), new ResourceLocation("quark", "root_item"));
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Item Tags";
    }
}
