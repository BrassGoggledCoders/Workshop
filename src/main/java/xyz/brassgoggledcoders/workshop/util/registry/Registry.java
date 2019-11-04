package xyz.brassgoggledcoders.workshop.util.registry;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicBlock;
import xyz.brassgoggledcoders.workshop.blocks.alembic.AlembicTile;
import xyz.brassgoggledcoders.workshop.item.BlockItemBase;
import xyz.brassgoggledcoders.workshop.item.ItemBase;


import static xyz.brassgoggledcoders.workshop.WorkShop.MOD_ID;
import static xyz.brassgoggledcoders.workshop.blocks.BlockProperties.*;
import static xyz.brassgoggledcoders.workshop.item.ItemProperties.*;
import static xyz.brassgoggledcoders.workshop.util.lib.NameLib.*;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry
{
    //TileEntity Registry
    //============================================================
    @SubscribeEvent
    public static void onRegisterTileEntity(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(AlembicTile::new, alembicblock).build(null));

    }

    //Block Registry
    //============================================================
    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event) {

        //Alembic
        event.getRegistry().register(new AlembicBlock(METAL_BLOCK, "alembic"));

    }

    //Item Registry
    //============================================================
    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {

        //ItemGroup Icon
        event.getRegistry().register(new ItemBase(ICON, "workicon"));

        //Alembic Outputs
        event.getRegistry().register(new ItemBase(ITEM, "adhesive_oils"));
        event.getRegistry().register(new ItemBase(ITEM, "distilled_water"));
        event.getRegistry().register(new ItemBase(ITEM, "tanin"));

        //Ingredients
        event.getRegistry().register(new ItemBase(ITEM, "salt"));
        event.getRegistry().register(new ItemBase(ITEM, "chalk"));
        event.getRegistry().register(new ItemBase(ITEM, "ash"));
        event.getRegistry().register(new ItemBase(ITEM, "silt"));
        event.getRegistry().register(new ItemBase(ITEM, "tea"));
        event.getRegistry().register(new ItemBase(ITEM, "medicinalroots"));



        //BlockItems
        event.getRegistry().register(new BlockItemBase(alembicblock, ITEM, "alembic"));

    }


}
