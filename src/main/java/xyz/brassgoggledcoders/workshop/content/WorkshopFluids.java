package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;

import java.util.Collection;

public class WorkshopFluids {
    private static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, Workshop.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Workshop.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Workshop.MOD_ID);

    public static final int BOTTLE_VOLUME = FluidAttributes.BUCKET_VOLUME / 3;

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

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> RESIN = new FluidRegistryObjectGroup<>("resin",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.RESIN_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.RESIN_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties RESIN_PROPERTIES = new ForgeFlowingFluid.Properties(RESIN, RESIN::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(16762920))
            .block(RESIN::getBlock)
            .bucket(RESIN::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> ADHESIVE_OILS = new FluidRegistryObjectGroup<>("adhesive_oils",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.ADHESIVE_OILS_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.ADHESIVE_OILS_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties ADHESIVE_OILS_PROPERTIES = new ForgeFlowingFluid.Properties(ADHESIVE_OILS, ADHESIVE_OILS::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(655360))
            .block(ADHESIVE_OILS::getBlock)
            .bucket(ADHESIVE_OILS::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> APPLE_JUICE = new FluidRegistryObjectGroup<>("apple_juice",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.APPLE_JUICE_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.APPLE_JUICE_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties APPLE_JUICE_PROPERTIES = new ForgeFlowingFluid.Properties(APPLE_JUICE, APPLE_JUICE::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(16724530))
            .block(APPLE_JUICE::getBlock)
            .bucket(APPLE_JUICE::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> CIDER = new FluidRegistryObjectGroup<>("cider",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.CIDER_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.CIDER_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties CIDER_PROPERTIES = new ForgeFlowingFluid.Properties(CIDER, CIDER::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(16762880))
            .block(CIDER::getBlock)
            .bucket(CIDER::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> CHERRY_JUICE = new FluidRegistryObjectGroup<>("cherry_juice",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.CHERRY_JUICE_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.CHERRY_JUICE_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties CHERRY_JUICE_PROPERTIES = new ForgeFlowingFluid.Properties(CHERRY_JUICE, CHERRY_JUICE::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(11797765))
            .block(CHERRY_JUICE::getBlock)
            .bucket(CHERRY_JUICE::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> GLACIAL_WATER = new FluidRegistryObjectGroup<>("glacial_water",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.GLACIAL_WATER_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.GLACIAL_WATER_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties GLACIAL_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(GLACIAL_WATER, GLACIAL_WATER::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(2631935))
            .block(GLACIAL_WATER::getBlock)
            .bucket(GLACIAL_WATER::getBucket);
    
    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> TEA = new FluidRegistryObjectGroup<>("tea_liquid",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.TEA_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.TEA_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties TEA_PROPERTIES = new ForgeFlowingFluid.Properties(TEA, TEA::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(13125120))
            .block(TEA::getBlock)
            .bucket(TEA::getBucket);


    public static void register(IEventBus modBus) {
        FLUIDS.register(modBus);
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
    }

    public static Collection<RegistryObject<Fluid>> getAllFluids() {
        return FLUIDS.getEntries();
    }

    public static Collection<RegistryObject<Block>> getAllBlocks() {
        return BLOCKS.getEntries();
    }
 }
