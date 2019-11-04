package xyz.brassgoggledcoders.workshop;


import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.workshop.registries.Blocks;
import xyz.brassgoggledcoders.workshop.registries.Items;
import xyz.brassgoggledcoders.workshop.registries.TileEntities;

import static xyz.brassgoggledcoders.workshop.WorkShop.MOD_ID;


//Main Class
@Mod(MOD_ID)
public class WorkShop
{
    public static final String MOD_ID = "workshop";

    private static final Logger LOGGER = LogManager.getLogger();

    public WorkShop() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        Blocks.register(modBus);
        Items.register(modBus);
        TileEntities.register(modBus);

    }


}
