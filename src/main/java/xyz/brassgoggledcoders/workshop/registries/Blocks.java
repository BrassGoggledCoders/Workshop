package xyz.brassgoggledcoders.workshop.registries;

import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.blocks.ObsidianPlate;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicBlock;
import xyz.brassgoggledcoders.workshop.util.WorkGroup;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class Blocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Block> OBSIDIAN_PLATE = BLOCKS.register("obsidian_plate", () -> new ObsidianPlate(PressurePlateBlock.Sensitivity.EVERYTHING,Block.Properties.create(Material.ROCK)));

    public static final RegistryObject<Item> OBSIDIAN_PLATE_ITEM = ITEMS.register("obsidian_plate", () -> new BlockItem(OBSIDIAN_PLATE.get(),new Item.Properties()));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

}
