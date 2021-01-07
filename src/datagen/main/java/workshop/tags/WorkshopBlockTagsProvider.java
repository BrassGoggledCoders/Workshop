package workshop.tags;

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
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Block Tags";
    }
}
