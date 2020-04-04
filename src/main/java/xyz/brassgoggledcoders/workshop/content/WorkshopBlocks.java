package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.block.*;
import xyz.brassgoggledcoders.workshop.block.press.PressBlock;
import xyz.brassgoggledcoders.workshop.tileentity.*;

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

    public static final BlockRegistryObjectGroup<Block, BlockItem, ?>[] CONCRETES = new BlockRegistryObjectGroup[DyeColor.values().length];
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> WHITE_REBARRED_CONCRETE =
            CONCRETES[0] = new BlockRegistryObjectGroup<>("white_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.WHITE).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> ORANGE_REBARRED_CONCRETE =
            CONCRETES[1] = new BlockRegistryObjectGroup<>("orange_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.ORANGE).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> MAGENTA_REBARRED_CONCRETE =
            CONCRETES[2] = new BlockRegistryObjectGroup<>("magenta_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.MAGENTA).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> LIGHT_BLUE_REBARRED_CONCRETE =
            CONCRETES[3] = new BlockRegistryObjectGroup<>("light_blue_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.LIGHT_BLUE).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> YELLOW_REBARRED_CONCRETE =
            CONCRETES[4] = new BlockRegistryObjectGroup<>("yellow_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.YELLOW).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> LIME_REBARRED_CONCRETE =
            CONCRETES[5] = new BlockRegistryObjectGroup<>("lime_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.LIME).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> PINK_REBARRED_CONCRETE =
            CONCRETES[6] = new BlockRegistryObjectGroup<>("pink_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.PINK).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> GRAY_REBARRED_CONCRETE =
            CONCRETES[7] = new BlockRegistryObjectGroup<>("gray_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.GRAY).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> LIGHT_GRAY_REBARRED_CONCRETE =
            CONCRETES[8] = new BlockRegistryObjectGroup<>("light_gray_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.LIGHT_GRAY).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> CYAN_REBARRED_CONCRETE =
            CONCRETES[9] = new BlockRegistryObjectGroup<>("cyan_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.CYAN).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> PURPLE_REBARRED_CONCRETE =
            CONCRETES[10] = new BlockRegistryObjectGroup<>("purple_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.PURPLE).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> BLUE_REBARRED_CONCRETE =
            CONCRETES[11] = new BlockRegistryObjectGroup<>("blue_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.BLUE).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> BROWN_REBARRED_CONCRETE =
            CONCRETES[12] = new BlockRegistryObjectGroup<>("brown_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.BROWN).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> GREEN_REBARRED_CONCRETE =
            CONCRETES[13] = new BlockRegistryObjectGroup<>("green_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.GREEN).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> RED_REBARRED_CONCRETE =
            CONCRETES[14] = new BlockRegistryObjectGroup<>("red_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.RED).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);
    public static final BlockRegistryObjectGroup<Block, BlockItem, ?> BLACK_REBARRED_CONCRETE =
            CONCRETES[15] = new BlockRegistryObjectGroup<>("black_rebarred_concrete", () -> new Block(Block.Properties.create(Material.ROCK, DyeColor.BLACK).hardnessAndResistance(5F)), blockItemCreator())
                    .register(BLOCKS, ITEMS);

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

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        TILE_ENTITIES.register(bus);
        ITEMS.register(bus);
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(Workshop.ITEM_GROUP));
    }
}
