package xyz.brassgoggledcoders.workshop;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.renderer.ChalkWritingTileEntityRenderer;
import xyz.brassgoggledcoders.workshop.renderer.PressTileEntityRenderer;
import xyz.brassgoggledcoders.workshop.renderer.SinteringTileEntityRenderer;

public class ClientEventHandler {
    public static void clientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.PRESS.getTileEntityType(), PressTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(), SinteringTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.CHALK_WRITING.getTileEntityType(), ChalkWritingTileEntityRenderer::new);

        RenderTypeLookup.setRenderLayer(WorkshopBlocks.SINTERING_FURNACE.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.PRESS.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.ALEMBIC.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.TEA_PLANT.getBlock(), RenderType.getCutout());
    }
}