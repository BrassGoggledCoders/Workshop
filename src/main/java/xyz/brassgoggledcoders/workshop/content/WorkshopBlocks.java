package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.blocks.BrokenAnvilBlock;
import xyz.brassgoggledcoders.workshop.blocks.ObsidianPlateBlock;
import xyz.brassgoggledcoders.workshop.blocks.TileBlock;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicBlock;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicTile;
import xyz.brassgoggledcoders.workshop.blocks.press.PressBlock;
import xyz.brassgoggledcoders.workshop.blocks.press.PressTile;
import xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel.SeasoningBarrelBlock;
import xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel.SeasoningBarrelTile;
import xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace.SinteringFurnaceBlock;
import xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace.SinteringFurnaceTile;
import xyz.brassgoggledcoders.workshop.blocks.spinningwheel.SpinningWheelBlock;
import xyz.brassgoggledcoders.workshop.blocks.spinningwheel.SpinningWheelTile;

import java.util.function.Function;

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

    public static final BlockRegistryObjectGroup<AlembicBlock, BlockItem, AlembicTile> ALEMBIC =
            new BlockRegistryObjectGroup<>("alembic", AlembicBlock::new, blockItemCreator(), AlembicTile::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<PressBlock, BlockItem, PressTile> PRESS =
            new BlockRegistryObjectGroup<>("press", PressBlock::new, blockItemCreator(), PressTile::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<SeasoningBarrelBlock, BlockItem, SeasoningBarrelTile> SEASONING_BARREL =
            new BlockRegistryObjectGroup<>("seasoning_barrel", SeasoningBarrelBlock::new, blockItemCreator(),
                    SeasoningBarrelTile::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<SinteringFurnaceBlock, BlockItem, SinteringFurnaceTile> SINTERING_FURNACE =
            new BlockRegistryObjectGroup<>("sintering_furnace", SinteringFurnaceBlock::new, blockItemCreator(),
                    SinteringFurnaceTile::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<SpinningWheelBlock, BlockItem, SpinningWheelTile> SPINNING_WHEEL =
            new BlockRegistryObjectGroup<>("spinning_wheel", SpinningWheelBlock::new, blockItemCreator(),
                    SpinningWheelTile::new)
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
