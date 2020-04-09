package xyz.brassgoggledcoders.workshop.datagen;


import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class WorkshopItemTagsProvider extends ItemTagsProvider {

    public static final Tag<Item> RAW_MEAT = new ItemTags.Wrapper(new ResourceLocation("forge", "raw_meats"));

    public WorkshopItemTagsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(RAW_MEAT).add(Items.RABBIT, Items.CHICKEN, Items.BEEF, Items.MUTTON, Items.PORKCHOP);
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Item Tags";
    }
}
