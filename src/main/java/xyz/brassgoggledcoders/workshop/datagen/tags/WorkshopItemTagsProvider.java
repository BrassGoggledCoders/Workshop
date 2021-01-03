package xyz.brassgoggledcoders.workshop.datagen.tags;


import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.capabilities.BottleCapabilityProvider;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;

public class WorkshopItemTagsProvider extends ItemTagsProvider {

    public static final ITag.INamedTag<Item> TEA_SEEDS = ItemTags.makeWrapperTag("forge:seeds/tea");
    public static final ITag.INamedTag<Item> RAW_MEAT = ItemTags.makeWrapperTag("forge:raw_meats");
    public static final ITag.INamedTag<Item> COLD = ItemTags.makeWrapperTag(Workshop.MOD_ID + ":cold");
    public static final ITag.INamedTag<Item> REBARRED_CONCRETE = ItemTags.makeWrapperTag(Workshop.MOD_ID + ":rebarred_concrete");
    public static final ITag.INamedTag<Item> ROOTS = ItemTags.makeWrapperTag(Workshop.MOD_ID + ":roots");
    public static final ITag.INamedTag<Item> TALLOW = ItemTags.makeWrapperTag("forge:tallow");
    public static final ITag.INamedTag<Item> SALT = ItemTags.makeWrapperTag("forge:dusts/salt");
    //Filled automatically by Titanium
    public static final ITag.INamedTag<Item> IRON_FILM = ItemTags.makeWrapperTag("forge:films/iron");

    public WorkshopItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper)
    {
        super(gen, blockTagProvider, Workshop.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerTags() {
        this.getOrCreateBuilder(RAW_MEAT).add(Items.RABBIT, Items.CHICKEN, Items.BEEF, Items.MUTTON, Items.PORKCHOP);
        this.getOrCreateBuilder(Tags.Items.SLIMEBALLS).add(BottleCapabilityProvider.getFilledBottle(new FluidStack(WorkshopFluids.ADHESIVE_OILS.getFluid().getFluid(), WorkshopFluids.BOTTLE_VOLUME)).getItem());
        this.getOrCreateBuilder(TEA_SEEDS).add(WorkshopBlocks.TEA_PLANT.getItem());
        this.getOrCreateBuilder(Tags.Items.SEEDS).add(WorkshopBlocks.TEA_PLANT.getItem());
        this.getOrCreateBuilder(COLD).add(Items.SNOW, Items.SNOW_BLOCK, Items.SNOWBALL, Items.ICE, Items.BLUE_ICE, Items.PACKED_ICE);
        WorkshopBlocks.CONCRETES.forEach(c -> this.getOrCreateBuilder(REBARRED_CONCRETE).add(c.getItem()));
        this.getOrCreateBuilder(TALLOW).add(WorkshopItems.TALLOW.get());
        this.getOrCreateBuilder(ROOTS).add(Items.POISONOUS_POTATO).addOptional(new ResourceLocation("quark", "root_item"));
        this.getOrCreateBuilder(SALT).add(WorkshopItems.SALT.get());
        this.getOrCreateBuilder(ItemTags.makeWrapperTag("forge:salt")).add(WorkshopItems.SALT.get());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Item Tags";
    }
}
