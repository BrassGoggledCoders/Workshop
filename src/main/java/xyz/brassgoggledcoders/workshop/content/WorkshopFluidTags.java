package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class WorkshopFluidTags {
    public static final TagKey<Fluid> BRINE = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("forge", "brine"));
}
