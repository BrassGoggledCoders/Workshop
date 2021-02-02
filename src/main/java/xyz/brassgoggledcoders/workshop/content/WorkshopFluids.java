package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.LavaInteractableFluidBlock;
import xyz.brassgoggledcoders.workshop.fluid.HoneyFluid;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    public static final ForgeFlowingFluid.Properties HONEY_PROPERTIES = properties(HONEY, "EBA937");

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> MEAD = new FluidRegistryObjectGroup<>("mead", () ->
            new ForgeFlowingFluid.Source(WorkshopFluids.MEAD_PROPERTIES), () ->
            new ForgeFlowingFluid.Flowing(WorkshopFluids.MEAD_PROPERTIES)
    ).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties MEAD_PROPERTIES = properties(MEAD, "EBA937");

    private static final Supplier<ForgeFlowingFluid.Source> brine = () ->
            new ForgeFlowingFluid.Source(WorkshopFluids.BRINE_PROPERTIES);
    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> BRINE = new FluidRegistryObjectGroup<>("brine", brine, () ->
            new ForgeFlowingFluid.Flowing(WorkshopFluids.BRINE_PROPERTIES), () -> new FlowingFluidBlock(brine, AbstractBlock.Properties.from(Blocks.WATER)) {
        @Override
        @SuppressWarnings("deprecated")
        public void onEntityCollision(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Entity entityIn) {
            BlockState blockstate = worldIn.getBlockState(pos.up());
            if (blockstate.isAir(worldIn, pos)) {
                entityIn.onEnterBubbleColumnWithAirAbove(false);
            } else {
                entityIn.onEnterBubbleColumn(false);
            }

        }
    }).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties BRINE_PROPERTIES = properties(BRINE, "17a2b8");

    public static final Supplier<ForgeFlowingFluid.Source> potash = () -> new ForgeFlowingFluid.Source(WorkshopFluids.POTASH_WATER_PROPERTIES);
    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> POTASH_WATER = new FluidRegistryObjectGroup<>("potash_water",
            potash, () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.POTASH_WATER_PROPERTIES),
            () -> new LavaInteractableFluidBlock(potash, AbstractBlock.Properties.from(Blocks.WATER), Blocks.ANDESITE::getDefaultState)).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties POTASH_WATER_PROPERTIES = properties(POTASH_WATER, "7DF9FF");

    public static final Supplier<ForgeFlowingFluid.Source> glacial = () -> new ForgeFlowingFluid.Source(WorkshopFluids.GLACIAL_WATER_PROPERTIES);
    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> GLACIAL_WATER = new FluidRegistryObjectGroup<>("glacial_water",
            glacial, () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.GLACIAL_WATER_PROPERTIES),
            () -> new LavaInteractableFluidBlock(glacial, AbstractBlock.Properties.from(Blocks.WATER), () -> WorkshopBlocks.SILT.getBlock().getDefaultState()) {
                @Override
                public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
                    entityIn.setMotionMultiplier(state, new Vector3d(0.25D, 0.05F, 0.25D));
                }
            }).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties GLACIAL_WATER_PROPERTIES = properties(GLACIAL_WATER, "3a5358");

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> DISTILLED_WATER = new FluidRegistryObjectGroup<>("distilled_water",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.DISTILLED_WATER_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.DISTILLED_WATER_PROPERTIES))
            .register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties DISTILLED_WATER_PROPERTIES = properties(DISTILLED_WATER, "d7eef2");

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> SEED_OIL = new FluidRegistryObjectGroup<>("seed_oil",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.SEED_OIL_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.SEED_OIL_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties SEED_OIL_PROPERTIES = properties(SEED_OIL, "562e33");

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> RESIN = new FluidRegistryObjectGroup<>("resin",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.RESIN_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.RESIN_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties RESIN_PROPERTIES = properties(RESIN, "c1a63c");

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> ADHESIVE_OILS = new FluidRegistryObjectGroup<>("adhesive_oils",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.ADHESIVE_OILS_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.ADHESIVE_OILS_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties ADHESIVE_OILS_PROPERTIES = properties(ADHESIVE_OILS, "3b0d0d");

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> APPLE_JUICE = new FluidRegistryObjectGroup<>("apple_juice",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.APPLE_JUICE_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.APPLE_JUICE_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties APPLE_JUICE_PROPERTIES = properties(APPLE_JUICE, "fff15e");

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> CIDER = new FluidRegistryObjectGroup<>("cider",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.CIDER_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.CIDER_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties CIDER_PROPERTIES = properties(CIDER, "ffc107");

    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> TEA = new FluidRegistryObjectGroup<>("tea",
            () -> new ForgeFlowingFluid.Source(WorkshopFluids.TEA_PROPERTIES), () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.TEA_PROPERTIES)).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties TEA_PROPERTIES = properties(TEA, "8B512F");

    private static final Supplier<ForgeFlowingFluid.Source> hellblood = () -> new ForgeFlowingFluid.Source(WorkshopFluids.HELLBLOOD_PROPERTIES);
    public static final FluidRegistryObjectGroup<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing> HELLBLOOD = new FluidRegistryObjectGroup<>("hellblood",
            hellblood, () -> new ForgeFlowingFluid.Flowing(WorkshopFluids.HELLBLOOD_PROPERTIES), () -> new LavaInteractableFluidBlock(hellblood, AbstractBlock.Properties.from(Blocks.LAVA), Blocks.BASALT::getDefaultState)).register(FLUIDS, BLOCKS, ITEMS);
    public static final ForgeFlowingFluid.Properties HELLBLOOD_PROPERTIES = properties(HELLBLOOD, "8c2727", "lava");

    public static void register(IEventBus modBus) {
        FLUIDS.register(modBus);
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
    }

    public static Collection<RegistryObject<Fluid>> getAllFluids() {
        return FLUIDS.getEntries().stream().filter(fluid -> !fluid.getId().getPath().contains("flowing")).collect(Collectors.toList());
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

    private static ForgeFlowingFluid.Properties properties(FluidRegistryObjectGroup<?, ?> fluid, String color) {
        return properties(fluid, color, "water");
    }

    private static ForgeFlowingFluid.Properties properties(FluidRegistryObjectGroup<?, ?> fluid, String color, String override) {
        return new ForgeFlowingFluid.Properties(fluid, fluid::getFlowing,
                FluidAttributes.builder(new ResourceLocation("minecraft", String.format("block/%s_still", override)),
                        new ResourceLocation("minecraft", String.format("block/%s_flow", override)))
                        .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
                        .color(fromHex(color)))
                .block(fluid::getBlock)
                .bucket(fluid::getBucket);
    }
}
