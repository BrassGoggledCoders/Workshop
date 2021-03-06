package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.workshop.Workshop;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class FluidRegistryObjectGroup<F extends FlowingFluid, FF extends Fluid> implements Supplier<F> {

    private final String name;
    private final Supplier<F> fluidCreator;
    private final Supplier<FF> flowingFluidCreator;
    private final Supplier<FlowingFluidBlock> blockCreator;

    private RegistryObject<F> fluid;
    private RegistryObject<FF> flowingFluid;
    private RegistryObject<FlowingFluidBlock> block;
    private RegistryObject<BucketItem> bucket;

    public FluidRegistryObjectGroup(String name, Supplier<F> fluidCreator, Supplier<FF> flowingFluidCreator) {
        this(name, fluidCreator, flowingFluidCreator, () -> new FlowingFluidBlock(fluidCreator, Block.Properties.from(Blocks.WATER).noDrops()));
    }

    public FluidRegistryObjectGroup(String name, Supplier<F> fluidCreator, Supplier<FF> flowingFluidCreator, Supplier<FlowingFluidBlock> blockCreator) {
        this.name = name;
        this.fluidCreator = fluidCreator;
        this.flowingFluidCreator = flowingFluidCreator;
        this.blockCreator = blockCreator; //TODO
    }

    @SuppressWarnings("UnusedReturnValue")
    public FluidRegistryObjectGroup<F, FF> register(DeferredRegister<Fluid> fluidRegistry, DeferredRegister<Block> blockRegistry) {
        fluid = fluidRegistry.register(name, fluidCreator);
        flowingFluid = fluidRegistry.register(name + "_flowing", flowingFluidCreator);
        block = blockRegistry.register(name, blockCreator);
        return this;
    }

    public FluidRegistryObjectGroup<F, FF> register(DeferredRegister<Fluid> fluidRegistry, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
        this.register(fluidRegistry, blockRegistry);
        bucket = itemRegistry.register(name + "_bucket", () -> new BucketItem(fluid, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(Workshop.ITEM_GROUP)));
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Nonnull
    public F getFluid() {
        return Objects.requireNonNull(fluid).get();
    }

    @Nonnull
    public FF getFlowing() {
        return Objects.requireNonNull(flowingFluid).get();
    }

    @Nonnull
    public FlowingFluidBlock getBlock() {
        return Objects.requireNonNull(block).get();
    }

    @Nullable
    public BucketItem getBucket() {
        return bucket.get();
    }

    @Override
    public F get() {
        return this.getFluid();
    }
}
