package xyz.brassgoggledcoders.workshop.tag;

import com.google.common.collect.ImmutableMap;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import xyz.brassgoggledcoders.workshop.content.FluidRegistryObjectGroup;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

import java.util.stream.Collectors;

public class WorkshopFluidTags {
    public static final ImmutableMap<String, ITag.INamedTag<Fluid>> TAGS = ImmutableMap.copyOf(WorkshopFluids.getAllFluids().stream().map(fluid -> fluid.getId().getPath())
            .collect(Collectors.toMap(name -> name, name -> FluidTags.makeWrapperTag("forge:" + name))));
    public static ITag.INamedTag<Fluid> DRINKABLE_WATER = FluidTags.makeWrapperTag("forge:drinkable_water");

    public static ITag.INamedTag<Fluid> getFluidTag(FluidRegistryObjectGroup<?,?> ourFluid) {
        return TAGS.get(ourFluid.getName());
    }
}
