package xyz.brassgoggledcoders.workshop;

import com.hrznstudio.titanium.module.Module;
import com.hrznstudio.titanium.module.ModuleController;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.brassgoggledcoders.workshop.blocks.BlockNames;
import xyz.brassgoggledcoders.workshop.blocks.press.PressTile;
import xyz.brassgoggledcoders.workshop.blocks.press.PressTileEntityRenderer;
import xyz.brassgoggledcoders.workshop.registries.Blocks;
import xyz.brassgoggledcoders.workshop.registries.Items;
import xyz.brassgoggledcoders.workshop.registries.Recipes;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

//Main Class
@Mod(MOD_ID)
public class Workshop extends ModuleController {
    public static final String MOD_ID = "workshop";

    public static Logger LOGGER = LogManager.getLogger();

    public Workshop() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        Recipes.register(modBus);
        Items.register(modBus);
        Blocks.register(modBus);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(PressTile.class, new PressTileEntityRenderer());
    }


        @Override
    protected void initModules() {
        Module.Builder blocks = Module.builder("blocks").description("Module for all the blocks in WorkShop");
        new BlockNames().generateFeatures().forEach(blocks::feature);
        addModule(blocks);
    }

}
