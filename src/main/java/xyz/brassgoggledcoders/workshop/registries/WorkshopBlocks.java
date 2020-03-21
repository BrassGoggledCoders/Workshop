package xyz.brassgoggledcoders.workshop.registries;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.blocks.BrokenAnvilBlock;
import xyz.brassgoggledcoders.workshop.blocks.ObsidianPlateBlock;

public class WorkshopBlocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Block> OBSIDIAN_PLATE = BLOCKS.register("obsidian_plate",
            () -> new ObsidianPlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    Block.Properties.create(Material.ROCK)));

    public static final RegistryObject<Item> OBSIDIAN_PLATE_ITEM = ITEMS.register("obsidian_plate",
            () -> new BlockItem(OBSIDIAN_PLATE.get(), new Item.Properties().group(Workshop.workshopTab)));

    public static final RegistryObject<Block> BROKEN_ANVIL = BLOCKS.register("broken_anvil",
            () -> new BrokenAnvilBlock(Block.Properties.create(Material.ANVIL)));

    public static final RegistryObject<Item> BROKEN_ANVIL_ITEM = ITEMS.register("broken_anvil",
            () -> new BlockItem(BROKEN_ANVIL.get(), new Item.Properties().group(Workshop.workshopTab)));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

}
