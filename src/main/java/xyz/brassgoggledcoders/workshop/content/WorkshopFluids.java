package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;

public class WorkshopFluids {
    private static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, Workshop.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Workshop.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Workshop.MOD_ID);


    public static final RegistryObject<FlowingFluid> BRINE = FLUIDS.register("brine", () ->
            new ForgeFlowingFluid.Source(WorkshopFluids.BRINE_PROPERTIES)
    );

    public static final RegistryObject<FlowingFluid> BRINE_FLOWING = FLUIDS.register("brine_flowing", () ->
            new ForgeFlowingFluid.Flowing(WorkshopFluids.BRINE_PROPERTIES)
    );

    public static final RegistryObject<FlowingFluidBlock> BRINE_BLOCK = BLOCKS.register("brine", () ->
            new FlowingFluidBlock(BRINE, Block.Properties.from(Blocks.WATER).noDrops()));

    public static RegistryObject<Item> BRINE_BUCKET = ITEMS.register("brine_bucket", () ->
            new BucketItem(BRINE, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(Workshop.ITEM_GROUP))
    );

    public static final ForgeFlowingFluid.Properties BRINE_PROPERTIES = new ForgeFlowingFluid.Properties(BRINE,
            BRINE_FLOWING, FluidAttributes.builder(new ResourceLocation("minecraft", "block/water_still"),
            new ResourceLocation("minecraft", "block/water_flow"))
            .overlay(new ResourceLocation("minecraft", "block/water_overlay"))
            .color(2980986))
            .block(BRINE_BLOCK)
            .bucket(BRINE_BUCKET);

    public static void register(IEventBus modBus) {
        FLUIDS.register(modBus);
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
    }
}
