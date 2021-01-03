package xyz.brassgoggledcoders.workshop.datagen.tags;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.brassgoggledcoders.workshop.Workshop;

import javax.annotation.Nonnull;

public class WorkshopBlockTagsProvider extends BlockTagsProvider {
    public WorkshopBlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Workshop.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerTags() {
        //NO-OP
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Block Tags";
    }
}
