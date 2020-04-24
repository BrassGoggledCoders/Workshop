package xyz.brassgoggledcoders.workshop.content;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.*;
import xyz.brassgoggledcoders.workshop.block.press.PressBlock;
import xyz.brassgoggledcoders.workshop.tileentity.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

@SuppressWarnings("unused")
public class WorkshopBlocks {
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    public static final BlockRegistryObjectGroup<ObsidianPlateBlock, BlockItem, ?> OBSIDIAN_PLATE =
            new BlockRegistryObjectGroup<>("obsidian_plate", ObsidianPlateBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<BrokenAnvilBlock, BlockItem, ?> BROKEN_ANVIL =
            new BlockRegistryObjectGroup<>("broken_anvil", BrokenAnvilBlock::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);

    public static final List<BlockRegistryObjectGroup<Block, BlockItem, ?>> CONCRETES = Stream.of(DyeColor.values())
            .map(dyeColor -> new BlockRegistryObjectGroup<>(dyeColor.getName() + "_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, dyeColor).hardnessAndResistance(5F)), blockItemCreator())
                .register(BLOCKS, ITEMS))
            .collect(Collectors.toList());

    public static final BlockRegistryObjectGroup<AlembicBlock, BlockItem, AlembicTileEntity> ALEMBIC =
            new BlockRegistryObjectGroup<>("alembic", AlembicBlock::new, blockItemCreator(), AlembicTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<PressBlock, BlockItem, PressTileEntity> PRESS =
            new BlockRegistryObjectGroup<>("press", PressBlock::new, blockItemCreator(), PressTileEntity::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<SeasoningBarrelBlock, BlockItem, SeasoningBarrelTileEntity> SEASONING_BARREL =
            new BlockRegistryObjectGroup<>("seasoning_barrel", SeasoningBarrelBlock::new, blockItemCreator(),
                    SeasoningBarrelTileEntity::new)
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

    public static final BlockRegistryObjectGroup<TeaPlantBlock, BlockNamedItem, ?> TEA_PLANT =
            new BlockRegistryObjectGroup<>("tea", TeaPlantBlock::new, (block) ->
                    new BlockNamedItem(WorkshopBlocks.TEA_PLANT.getBlock(), new Item.Properties().group(Workshop.ITEM_GROUP))).register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<BellowsBlock, BlockItem, ?> BELLOWS = new BlockRegistryObjectGroup<>("bellows", BellowsBlock::new, blockItemCreator())
            .register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<CollectorBlock, BlockItem, CollectorTileEntity> COLLECTOR = new BlockRegistryObjectGroup<>("collector", CollectorBlock::new, blockItemCreator(), CollectorTileEntity::new)
            .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<ScrapBinBlock, BlockItem, ScrapBinTileEntity> SCRAP_BIN = new BlockRegistryObjectGroup<ScrapBinBlock, BlockItem, ScrapBinTileEntity>("scrap_bin", ScrapBinBlock::new, blockItemCreator(), ScrapBinTileEntity::new)
            .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        TILE_ENTITIES.register(bus);
        ITEMS.register(bus);
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(Workshop.ITEM_GROUP));
    }
}
