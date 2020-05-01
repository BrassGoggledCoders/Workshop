package xyz.brassgoggledcoders.workshop.datagen.tags;


import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.workshop.capabilities.BottleCapabilityProvider;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
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
    }

    @Override
    @Nonnull
    public String getName() {
        return "Workshop Fluid Tags";
    }
}
