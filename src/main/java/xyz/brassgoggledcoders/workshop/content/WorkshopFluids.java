package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.fluid.HoneyFluid;

import java.util.Collection;
import java.util.function.Supplier;

public class WorkshopFluids {
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Workshop.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Workshop.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Workshop.MOD_ID);

    public static final int BOTTLE_VOLUME = 333;//mB

    public static final Material HONEY_MATERIAL = (new Material.Builder(MaterialColor.WATER)).doesNotBlockMovement().notSolid().replaceable().liquid().build();
    private static final Supplier<HoneyFluid.Source> honey = () -> new HoneyFluid.Source(WorkshopFluids.HONEY_PROPERTIES);
    public static final FluidRegistryObjectGroup<HoneyFluid.Source, HoneyFluid.Flowing> HONEY = new FluidRegistryObjectGroup<>("honey",
            honey, () -> new HoneyFluid.Flowing(WorkshopFluids.HONEY_PROPERTIES), () -> new FlowingFluidBlock(honey, Block.Properties.create(HONEY_MATERIAL).noDrops()) {
        @Override
        public boolean isStickyBlock(BlockState state) {
            return state.getFluidState().isSource();
        }
    }).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties HONEY_PROPERTIES = new ForgeFlowingFluid.Properties(HONEY, HONEY::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("EBA937")))
            .block(HONEY::getBlock)
            .bucket(HONEY::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> MEAD = new FluidRegistryObjectGroup<>("mead", () ->
            new ForgeFlowingFluid.Source(WorkshopFluids.MEAD_PROPERTIES), () ->
            new ForgeFlowingFluid.Flowing(WorkshopFluids.MEAD_PROPERTIES)
    ).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties MEAD_PROPERTIES = new ForgeFlowingFluid.Properties(MEAD,
            MEAD::getFlowing, FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
            new ResourceLocation("minecraft", "block/water_flow"))
            .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
            .color(fromHex("EBA937")))
            .block(MEAD::getBlock)
            .bucket(MEAD::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> BRINE = new FluidRegistryObjectGroup<>("brine", () ->
            new ForgeFlowingFluid.Source(WorkshopFluids.BRINE_PROPERTIES), () ->
            new ForgeFlowingFluid.Flowing(WorkshopFluids.BRINE_PROPERTIES)
    ).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties BRINE_PROPERTIES = new ForgeFlowingFluid.Properties(BRINE,
            BRINE::getFlowing, FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
            new ResourceLocation("minecraft", "block/water_flow"))
            .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
            .color(fromHex("17a2b8")))
            .block(BRINE::getBlock)
            .bucket(BRINE::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> DISTILLED_WATER = new FluidRegistryObjectGroup<>("distilled_water",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.DISTILLED_WATER_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.DISTILLED_WATER_PROPERTIES))
            .register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties DISTILLED_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(DISTILLED_WATER, DISTILLED_WATER::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("d7eef2")))
            .block(DISTILLED_WATER::getBlock)
            .bucket(DISTILLED_WATER::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> SEED_OIL = new FluidRegistryObjectGroup<>("seed_oil",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.SEED_OIL_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.SEED_OIL_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties SEED_OIL_PROPERTIES = new ForgeFlowingFluid.Properties(SEED_OIL, SEED_OIL::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("562e33")))
            .block(SEED_OIL::getBlock)
            .bucket(SEED_OIL::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> RESIN = new FluidRegistryObjectGroup<>("resin",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.RESIN_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.RESIN_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties RESIN_PROPERTIES = new ForgeFlowingFluid.Properties(RESIN, RESIN::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("c1a63c")))
            .block(RESIN::getBlock)
            .bucket(RESIN::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> ADHESIVE_OILS = new FluidRegistryObjectGroup<>("adhesive_oils",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.ADHESIVE_OILS_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.ADHESIVE_OILS_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties ADHESIVE_OILS_PROPERTIES = new ForgeFlowingFluid.Properties(ADHESIVE_OILS, ADHESIVE_OILS::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("3b0d0d")))
            .block(ADHESIVE_OILS::getBlock)
            .bucket(ADHESIVE_OILS::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> APPLE_JUICE = new FluidRegistryObjectGroup<>("apple_juice",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.APPLE_JUICE_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.APPLE_JUICE_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties APPLE_JUICE_PROPERTIES = new ForgeFlowingFluid.Properties(APPLE_JUICE, APPLE_JUICE::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("fff15e")))
            .block(APPLE_JUICE::getBlock)
            .bucket(APPLE_JUICE::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> CIDER = new FluidRegistryObjectGroup<>("cider",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.CIDER_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.CIDER_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties CIDER_PROPERTIES = new ForgeFlowingFluid.Properties(CIDER, CIDER::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("ffc107")))
            .block(CIDER::getBlock)
            .bucket(CIDER::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> TEA = new FluidRegistryObjectGroup<>("tea_liquid",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.TEA_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.TEA_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties TEA_PROPERTIES = new ForgeFlowingFluid.Properties(TEA, TEA::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
                    new ResourceLocation("minecraft", "block/water_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("8B512F")))
            .block(TEA::getBlock)
            .bucket(TEA::getBucket);

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> HELLBLOOD = new FluidRegistryObjectGroup<>("hellblood",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.HELLBLOOD_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.HELLBLOOD_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);

    public static final ForgeFlowingFluid.Properties HELLBLOOD_PROPERTIES = new ForgeFlowingFluid.Properties(HELLBLOOD, HELLBLOOD::getFlowing,
            FluidAttributes.builder(new ResourceLocation("minecraft", "block/lava_still"),
                    new ResourceLocation("minecraft", "block/lava_flow"))
                    .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                    .color(fromHex("8c2727")))
            .block(HELLBLOOD::getBlock)
            .bucket(HELLBLOOD::getBucket);


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

    public static int fromHex(String text) {
        if (text.length() == 6) {
            text = "FF" + text;
        }
        return (int) Long.parseLong(text, 16);
    }
}
