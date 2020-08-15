package xyz.brassgoggledcoders.workshop.datagen.tags;


import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import javax.annotation.Nonnull;

public class WorkshopFluidTagsProvider extends FluidTagsProvider {

    public static final Tag<Fluid> BRINE = new FluidTags.Wrapper(new ResourceLocation("forge", "brine"));

    public WorkshopFluidTagsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        this.getBuilder(BRINE).add(WorkshopFluids.BRINE.getFluid());
        this.getBuilder(FluidTags.WATER).add(WorkshopFluids.DISTILLED_WATER.getFluid(), WorkshopFluids.BRINE.getFluid());
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Fluid Tags";
    }
}
