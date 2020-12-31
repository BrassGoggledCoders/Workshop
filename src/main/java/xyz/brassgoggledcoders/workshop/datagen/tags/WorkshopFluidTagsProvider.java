package xyz.brassgoggledcoders.workshop.datagen.tags;


import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import javax.annotation.Nonnull;

public class WorkshopFluidTagsProvider extends FluidTagsProvider {

    public static final ITag.INamedTag<Fluid> BRINE = FluidTags.makeWrapperTag("forge:brine");

    public WorkshopFluidTagsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(BRINE).add(WorkshopFluids.BRINE.getFluid());
        this.getOrCreateBuilder(FluidTags.WATER).add(WorkshopFluids.DISTILLED_WATER.getFluid(), WorkshopFluids.BRINE.getFluid());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Fluid Tags";
    }
}
