package workshop.tags;


import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;
import xyz.brassgoggledcoders.workshop.tag.WorkshopFluidTags;

import javax.annotation.Nonnull;

public class WorkshopFluidTagsProvider extends FluidTagsProvider {

    public WorkshopFluidTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Workshop.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        WorkshopFluids.getAllFluids().forEach(fluid -> this.getOrCreateBuilder(FluidTags.makeWrapperTag("forge:" + fluid.getId().getPath())).add(fluid.get()));
        this.getOrCreateBuilder(FluidTags.WATER).add(WorkshopFluids.DISTILLED_WATER.getFluid(),
                WorkshopFluids.BRINE.getFluid(), WorkshopFluids.POTASH_WATER.getFluid(), WorkshopFluids.GLACIAL_WATER.getFluid());
        this.getOrCreateBuilder(WorkshopFluidTags.DRINKABLE_WATER).add(WorkshopFluids.DISTILLED_WATER.getFluid(), WorkshopFluids.GLACIAL_WATER.getFluid(), Fluids.WATER.getFluid());
        this.getOrCreateBuilder(FluidTags.makeWrapperTag("forge:plantoil")).add(WorkshopFluids.SEED_OIL.getFluid());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Fluid Tags";
    }
}
