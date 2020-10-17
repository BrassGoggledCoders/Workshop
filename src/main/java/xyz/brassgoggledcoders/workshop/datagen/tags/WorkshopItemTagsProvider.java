package xyz.brassgoggledcoders.workshop.datagen.tags;


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
import xyz.brassgoggledcoders.workshop.content.WorkshopResourcePlugin;

import javax.annotation.Nonnull;

public class WorkshopItemTagsProvider extends ItemTagsProvider {

    public static final Tag<Item> TEA_SEEDS = new ItemTags.Wrapper(new ResourceLocation("forge", "seeds/tea"));
    public static final Tag<Item> RAW_MEAT = new ItemTags.Wrapper(new ResourceLocation("forge", "raw_meats"));
    public static final Tag<Item> COLD = new ItemTags.Wrapper(new ResourceLocation(Workshop.MOD_ID, "cold"));
    public static final Tag<Item> REBARRED_CONCRETE = new ItemTags.Wrapper(new ResourceLocation(Workshop.MOD_ID, "rebarred_concrete"));
    public static final Tag<Item> ROOTS = new ItemTags.Wrapper(new ResourceLocation(Workshop.MOD_ID, "roots"));
    public static final Tag<Item> TALLOW = new ItemTags.Wrapper(new ResourceLocation("forge", "tallow"));
    public static final Tag<Item> SALT = new ItemTags.Wrapper(new ResourceLocation("forge", "dusts/salt"));
    //Filled automatically by Titanium
    public static final Tag<Item> IRON_FILM = new ItemTags.Wrapper(new ResourceLocation("forge", "films/iron"));
    public static final Tag<Item> COPPER_BLOCKS = new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/copper"));
    public static final Tag<Item> SILVER_BLOCKS = new ItemTags.Wrapper(new ResourceLocation("forge", "storage_blocks/silver"));
    public static final Tag<Item> COPPER_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/copper"));
    public static final Tag<Item> SILVER_INGOTS = new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/silver"));
    public static final Tag<Item> COPPER_NUGGETS = new ItemTags.Wrapper(new ResourceLocation("forge", "nuggets/copper"));
    public static final Tag<Item> SILVER_NUGGETS = new ItemTags.Wrapper(new ResourceLocation("forge", "nuggets/silver"));

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
        this.getBuilder(SALT).add(WorkshopItems.SALT.get());
        this.getBuilder(new ItemTags.Wrapper(new ResourceLocation("forge", "salt"))).add(WorkshopItems.SALT.get());
        this.getBuilder(COPPER_BLOCKS).add(WorkshopResourcePlugin.COPPER_BLOCK.asItem());
        this.getBuilder(SILVER_BLOCKS).add(WorkshopResourcePlugin.SILVER_BLOCK.asItem());
        this.getBuilder(COPPER_INGOTS).add(WorkshopResourcePlugin.COPPER_INGOT);
        this.getBuilder(SILVER_INGOTS).add(WorkshopResourcePlugin.SILVER_INGOT);
        this.getBuilder(COPPER_NUGGETS).add(WorkshopResourcePlugin.COPPER_NUGGET);
        this.getBuilder(SILVER_NUGGETS).add(WorkshopResourcePlugin.SILVER_NUGGET);
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Item Tags";
    }
}
