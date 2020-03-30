package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;

public class WorkshopFluids {
    private static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, Workshop.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Workshop.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Workshop.MOD_ID);


    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> BRINE = new FluidRegistryObjectGroup<>("brine", () ->
            new ForgeFlowingFluid.Source(WorkshopFluids.BRINE_PROPERTIES), () ->
            new ForgeFlowingFluid.Flowing(WorkshopFluids.BRINE_PROPERTIES)
    ).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties BRINE_PROPERTIES = new ForgeFlowingFluid.Properties(BRINE,
            BRINE::getFlowing, FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
            new ResourceLocation("minecraft", "block/water_flow"))
            .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
            .color(2980986))
            .block(BRINE::getBlock)
            .bucket(BRINE::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> DISTILLED_WATER = new FluidRegistryObjectGroup<>("distilled_water",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.DISTILLED_WATER_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.DISTILLED_WATER_PROPERTIES))
            .register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties DISTILLED_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(DISTILLED_WATER, DISTILLED_WATER::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
            new ResourceLocation("minecraft", "block/water_flow"))
            .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
            .color(16777215))
            .block(DISTILLED_WATER::getBlock)
            .bucket(DISTILLED_WATER::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> SEED_OIL = new FluidRegistryObjectGroup<>("seed_oil",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.SEED_OIL_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.SEED_OIL_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties SEED_OIL_PROPERTIES = new ForgeFlowingFluid.Properties(SEED_OIL, SEED_OIL::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
            new ResourceLocation("minecraft", "block/water_flow"))
            .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
            .color(5184271))
            .block(SEED_OIL::getBlock)
            .bucket(SEED_OIL::getBucket);


    public static void register(IEventBus modBus) {
        FLUIDS.register(modBus);
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
    }
}
