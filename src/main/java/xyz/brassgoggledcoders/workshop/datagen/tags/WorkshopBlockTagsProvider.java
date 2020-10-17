package xyz.brassgoggledcoders.workshop.datagen.tags;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopResourcePlugin;

public class WorkshopBlockTagsProvider extends BlockTagsProvider {
    public static final Tag<Block> COPPER_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/copper"));
    public static final Tag<Block> SILVER_BLOCKS = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/silver"));


    public WorkshopBlockTagsProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(COPPER_BLOCKS).add(WorkshopResourcePlugin.COPPER_BLOCK);
        this.getBuilder(SILVER_BLOCKS).add(WorkshopResourcePlugin.SILVER_BLOCK);
    }
}
