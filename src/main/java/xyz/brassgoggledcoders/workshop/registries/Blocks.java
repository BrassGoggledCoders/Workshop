package xyz.brassgoggledcoders.workshop.registries;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicBlock;
import xyz.brassgoggledcoders.workshop.util.WorkGroup;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class Blocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    //Alembic

    public static final RegistryObject<Block> ALEMBIC_BLOCK = BLOCKS.register("alembic",
            () -> new AlembicBlock(Block.Properties.create(Material.IRON)
                    .sound(SoundType.METAL)
                    .hardnessAndResistance(5.0F)
            ));

    public static final RegistryObject<Block> PRESS_BLOCK = BLOCKS.register("press",
            () -> new AlembicBlock(Block.Properties.create(Material.IRON)
                    .sound(SoundType.METAL)
                    .hardnessAndResistance(5.0F)
            ));
    public static final RegistryObject<Block> SINTERING_FURNACE_BLOCK = BLOCKS.register("sintering_furnace",
            () -> new AlembicBlock(Block.Properties.create(Material.IRON)
                    .sound(SoundType.METAL)
                    .hardnessAndResistance(5.0F)
            ));
    public static final RegistryObject<Block> SEASONING_BARREL_BLOCK = BLOCKS.register("seasoning_barrel",
            () -> new AlembicBlock(Block.Properties.create(Material.IRON)
                    .sound(SoundType.METAL)
                    .hardnessAndResistance(5.0F)
            ));
    public static final RegistryObject<Block> SPINNING_WHEEL_BLOCK = BLOCKS.register("spinning_wheel",
            () -> new AlembicBlock(Block.Properties.create(Material.IRON)
                    .sound(SoundType.METAL)
                    .hardnessAndResistance(5.0F)
            ));
    public static final RegistryObject<Block> STRANGLE_GRASS_BLOCK = BLOCKS.register("stranglegrass",
            () -> new AlembicBlock(Block.Properties.create(Material.IRON)
                    .sound(SoundType.METAL)
                    .hardnessAndResistance(5.0F)
            ));
    public static final RegistryObject<Block> REBARRED_CONCRETE = BLOCKS.register("rebarred_concrete",
            () -> new AlembicBlock(Block.Properties.create(Material.IRON)
                    .sound(SoundType.METAL)
                    .hardnessAndResistance(5.0F)
            ));

    //BlockItems

    public static final RegistryObject<Item> ALEMBIC_ITEM = ITEMS.register("alembic",
            () -> new BlockItem(ALEMBIC_BLOCK.get(), new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> PRESS_ITEM = ITEMS.register("press",
            () -> new BlockItem(PRESS_BLOCK.get(), new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> SINTERING_FURNACE_ITEM = ITEMS.register("sintering_furnace",
            () -> new BlockItem(SINTERING_FURNACE_BLOCK.get(), new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> SEASONING_BARREL_ITEM = ITEMS.register("seasoning_barrel",
            () -> new BlockItem(SEASONING_BARREL_BLOCK.get(), new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> SPINNING_WHEEL_ITEM = ITEMS.register("spinning_wheel",
            () -> new BlockItem(SPINNING_WHEEL_BLOCK.get(), new Item.Properties().group(WorkGroup.instance)));
    public static final RegistryObject<Item> STRANGLE_GRASS_ITEM = ITEMS.register("stranglegrass",
            () -> new BlockItem(SPINNING_WHEEL_BLOCK.get(), new Item.Properties().group(WorkGroup.instance)));


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

}
