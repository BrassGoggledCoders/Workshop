package xyz.brassgoggledcoders.workshop;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.workshop.util.config.Config;

import static xyz.brassgoggledcoders.workshop.WorkShop.MOD_ID;


//Main Class
@Mod(MOD_ID)
public class WorkShop
{
    public static final String MOD_ID = "workshop";

    private static final Logger LOGGER = LogManager.getLogger();

    public WorkShop() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) { }

    private void doClientStuff(final FMLClientSetupEvent event) { }

    private void enqueueIMC(final InterModEnqueueEvent event) { }

    private void processIMC(final InterModProcessEvent event) { }

}
