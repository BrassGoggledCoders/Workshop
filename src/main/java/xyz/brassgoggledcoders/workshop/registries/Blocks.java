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

import static xyz.brassgoggledcoders.workshop.WorkShop.MOD_ID;

public class Blocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);


    //Alembic

    public static final RegistryObject<Block> ALEMBICBLOCK = BLOCKS.register("alembic",
            () -> new AlembicBlock(Block.Properties.create(Material.IRON)
            .sound(SoundType.METAL)
            .hardnessAndResistance(5.0F)
    ));





    //BlockItems

    public static final RegistryObject<Item> ALEMBICITEM = ITEMS.register("alembic",
            () -> new BlockItem(ALEMBICBLOCK.get(), new Item.Properties().group(WorkGroup.instance)));


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

}
