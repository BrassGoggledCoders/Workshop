package xyz.brassgoggledcoders.workshop.event;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.compat.patchouli.PageTypes;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.renderer.DryingBasinTileEntityRenderer;
import xyz.brassgoggledcoders.workshop.renderer.PressTileEntityRenderer;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEventHandler {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.PRESS.getTileEntityType(), PressTileEntityRenderer::new);
        //ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(), SinteringTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.DRYING_BASIN.getTileEntityType(), DryingBasinTileEntityRenderer::new);
        //ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.MORTAR.getTileEntityType(), MortarTileEntityRenderer::new);

        RenderTypeLookup.setRenderLayer(WorkshopBlocks.SINTERING_FURNACE.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.PRESS.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.ALEMBIC.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.TEA_PLANT.getBlock(), RenderType.getCutout());

        if (ModList.get().isLoaded("patchouli")) {
            PageTypes.registerPageTypes();
        }
    }
}
