package workshop.tags;

import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.WorkshopBlockTags;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;

import javax.annotation.Nonnull;

public class WorkshopBlockTagsProvider extends BlockTagsProvider {
    public WorkshopBlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Workshop.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerTags() {
        WorkshopBlocks.CONCRETES.forEach(c -> this.getOrCreateBuilder(WorkshopBlockTags.REBARRED_CONCRETE).add(c.getBlock()));
        this.getOrCreateBuilder(WorkshopBlockTags.VERY_HOT).add(Blocks.LAVA, Blocks.MAGMA_BLOCK);
        this.getOrCreateBuilder(WorkshopBlockTags.HOT).addTag(WorkshopBlockTags.VERY_HOT).add(Blocks.FIRE, Blocks.SOUL_FIRE, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
        this.getOrCreateBuilder(WorkshopBlockTags.STRIPPED_LOGS).add(Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_DARK_OAK_LOG,
                Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_SPRUCE_LOG);
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Block Tags";
    }
}
