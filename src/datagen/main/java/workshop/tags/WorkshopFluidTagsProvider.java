package workshop.tags;


import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import javax.annotation.Nonnull;

public class WorkshopFluidTagsProvider extends FluidTagsProvider {

    public static final ITag.INamedTag<Fluid> BRINE = FluidTags.makeWrapperTag("forge:brine");
    public static final ITag.INamedTag<Fluid> HONEY = FluidTags.makeWrapperTag("forge:honey");

    public WorkshopFluidTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(BRINE).add(WorkshopFluids.BRINE.getFluid());
        this.getOrCreateBuilder(HONEY).add(WorkshopFluids.HONEY.getFluid());
        this.getOrCreateBuilder(FluidTags.WATER).add(WorkshopFluids.DISTILLED_WATER.getFluid(),
                WorkshopFluids.BRINE.getFluid(), WorkshopFluids.POTASH_WATER.getFluid(), WorkshopFluids.GLACIAL_WATER.getFluid());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Fluid Tags";
    }
}
