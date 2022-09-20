package xyz.brassgoggledcoders.workshop.content;

import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import xyz.brassgoggledcoders.workshop.Workshop;

public class WorkshopFluids {

    public static final FluidEntry<Flowing> BRINE = Workshop.getRegistrate()
            .object("brine")
            .fluid(
                    new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow")
            )
            .attributes(builder -> builder.color(2980986)
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
            )
            .tag(WorkshopFluidTags.BRINE)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .model((context, provider) -> provider.withExistingParent(
                                    context.getName(),
                                    new ResourceLocation("forge", "item/bucket")
                            )
                            .customLoader(DynamicBucketModelBuilder::begin)
                            .fluid(context.get().getFluid())
                            .applyTint(true)
            )
            .build()
            .register();

    public static void setup() {

    }
}
