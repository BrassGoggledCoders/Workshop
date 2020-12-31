package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.*;
import xyz.brassgoggledcoders.workshop.tileentity.*;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

@SuppressWarnings("unused")
public class WorkshopBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final BlockRegistryObjectGroup<ObsidianPlateBlock, BlockItem, ObsidianPlateTileEntity> OBSIDIAN_PLATE =
            new BlockRegistryObjectGroup<>("obsidian_plate", ObsidianPlateBlock::new, blockItemCreator(), ObsidianPlateTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<BrokenAnvilBlock, BlockItem, ?> BROKEN_ANVIL =
            new BlockRegistryObjectGroup<>("broken_anvil", BrokenAnvilBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);

    public static final List<BlockRegistryObjectGroup<Block, BlockItem, ?>> CONCRETES = Stream.of(DyeColor.values())
            .map(dyeColor -> new BlockRegistryObjectGroup<>(dyeColor.name() + "_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, dyeColor).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS))
            .collect(Collectors.toList());

    public static final BlockRegistryObjectGroup<AlembicBlock, BlockItem, AlembicTileEntity> ALEMBIC =
            new BlockRegistryObjectGroup<>("alembic", AlembicBlock::new, blockItemCreator(), AlembicTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<PressBlock, BlockItem, PressTileEntity> PRESS =
            new BlockRegistryObjectGroup<>("press", PressBlock::new, blockItemCreator(), PressTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    //TODO Neither of these need items, and the arm is purely a dummy block for rendering, which is dumb...

    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> PRESS_ARM =
            new BlockRegistryObjectGroup<>("press_arm",() -> new Block(Block.Properties.create(Material.BAMBOO)), blockItemCreatorNoGroup()).register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<PressTopBlock, BlockItem, ?> PRESS_TOP =
            new BlockRegistryObjectGroup<>("press_top", PressTopBlock::new, blockItemCreatorNoGroup()).register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<SeasoningBarrelBlock, BlockItem, SeasoningBarrelTileEntity> SEASONING_BARREL =
            new BlockRegistryObjectGroup<>("seasoning_barrel", SeasoningBarrelBlock::new, blockItemCreator(),
                    SeasoningBarrelTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<MoltenChamberBlock, BlockItem, MoltenChamberTileEntity> MOLTEN_CHAMBER =
            new BlockRegistryObjectGroup<>("molten_chamber", MoltenChamberBlock::new, blockItemCreator(),
                    MoltenChamberTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<SinteringFurnaceBlock, BlockItem, SinteringFurnaceTileEntity> SINTERING_FURNACE =
            new BlockRegistryObjectGroup<>("sintering_furnace", SinteringFurnaceBlock::new, blockItemCreator(),
                    SinteringFurnaceTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<SpinningWheelBlock, BlockItem, SpinningWheelTileEntity> SPINNING_WHEEL =
            new BlockRegistryObjectGroup<>("spinning_wheel", SpinningWheelBlock::new, blockItemCreator(),
                    SpinningWheelTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<SealedBarrelBlock, BlockItem, SealedBarrelTileEntity> SEALED_BARREL =
            new BlockRegistryObjectGroup<>("sealed_barrel", SealedBarrelBlock::new, blockItemCreator(),
                    SealedBarrelTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<MortarBlock, BlockItem, MortarTileEntity> MORTAR =
            new BlockRegistryObjectGroup<>("mortar", MortarBlock::new, blockItemCreator(),
                    MortarTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<DryingBasinBlock, BlockItem, DryingBasinTileEntity> DRYING_BASIN =
            new BlockRegistryObjectGroup<>("drying_basin", DryingBasinBlock::new, blockItemCreator(),
                    DryingBasinTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<TeaPlantBlock, BlockNamedItem, ?> TEA_PLANT =
            new BlockRegistryObjectGroup<>("tea", TeaPlantBlock::new, (block) ->
                    new BlockNamedItem(WorkshopBlocks.TEA_PLANT.getBlock(), new Item.Properties().group(Workshop.ITEM_GROUP))).register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<BellowsBlock, BlockItem, ?> BELLOWS = new BlockRegistryObjectGroup<>("bellows", BellowsBlock::new, blockItemCreator())
            .register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<CollectorBlock, BlockItem, CollectorTileEntity> COLLECTOR = new BlockRegistryObjectGroup<>("collector", CollectorBlock::new, blockItemCreator(), CollectorTileEntity::new)
            .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<ScrapBinBlock, BlockItem, ScrapBinTileEntity> SCRAP_BIN = new BlockRegistryObjectGroup<>("scrap_bin", ScrapBinBlock::new, blockItemCreator(), ScrapBinTileEntity::new)
            .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<FluidFunnelBlock, BlockItem, FluidFunnelTileEntity> FLUID_FUNNEL = new BlockRegistryObjectGroup<>("fluid_funnel", FluidFunnelBlock::new, blockItemCreator(), FluidFunnelTileEntity::new)
            .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<SiloBarrelBlock, BlockItem, SiloBarrelTileEntity> SILO_BARREL = new BlockRegistryObjectGroup<>("silo_barrel", SiloBarrelBlock::new, blockItemCreator(), SiloBarrelTileEntity::new)
            .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<RotatedPillarBlock, BlockItem, ?> SEASONED_LOG =
            new BlockRegistryObjectGroup<>("seasoned_log", () -> createLogBlock(MaterialColor.ADOBE, MaterialColor.CLAY), blockItemCreator())
            .register(BLOCKS, ITEMS);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        TILE_ENTITIES.register(bus);
        ITEMS.register(bus);
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(Workshop.ITEM_GROUP));
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreatorNoGroup() {
        return block -> new BlockItem(block, new Item.Properties());
    }

    public static Collection<RegistryObject<Block>> getAllBlocks() {
        return BLOCKS.getEntries();
    }

    //Copied and pasted from vanilla
    private static RotatedPillarBlock createLogBlock(MaterialColor topColor, MaterialColor barkColor) {
        return new RotatedPillarBlock(AbstractBlock.Properties.create(Material.WOOD, (p_lambda$createLogBlock$36_2_) -> p_lambda$createLogBlock$36_2_.get(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
    }
}
