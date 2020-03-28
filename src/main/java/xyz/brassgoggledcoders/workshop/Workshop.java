package xyz.brassgoggledcoders.workshop;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.tab.TitaniumTab;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.brassgoggledcoders.workshop.blocks.press.PressTileEntityRenderer;
import xyz.brassgoggledcoders.workshop.blocks.sinteringfurnace.SinteringTileEntityRenderer;
import xyz.brassgoggledcoders.workshop.registries.*;

//Main Class
@Mod(MOD_ID)
public class Workshop extends ModuleController {
    public static final String MOD_ID = "workshop";

    public static Logger LOGGER = LogManager.getLogger();

    public static ItemGroup ITEM_GROUP = new TitaniumTab(MOD_ID,
            () -> new ItemStack(net.minecraft.block.Blocks.ANVIL));// TODO ICON

    public Workshop() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        WorkshopRecipes.register(modBus);
        WorkshopItems.register(modBus);
        WorkshopBlocks.register(modBus);
    }

    @Override
    protected void initModules() {

    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.PRESS.getTileEntityType(), PressTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(), SinteringTileEntityRenderer::new);

        RenderTypeLookup.setRenderLayer(WorkshopBlocks.SINTERING_FURNACE.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.PRESS.getBlock(), RenderType.getCutout());
    }
}
