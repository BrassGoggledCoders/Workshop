package xyz.brassgoggledcoders.workshop;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.renderer.*;

public class ClientEventHandler {


    public static void clientStartUpEvent() {
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.PRESS.getTileEntityType(), PressTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.SINTERING_FURNACE.getTileEntityType(), SinteringTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.CHALK_WRITING.getTileEntityType(), ChalkWritingTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.DRYING_BASIN.getTileEntityType(), DryingBasinTileEntityRenderer::new);
        //ClientRegistry.bindTileEntityRenderer(WorkshopBlocks.MORTAR.getTileEntityType(), MortarTileEntityRenderer::new);

        RenderTypeLookup.setRenderLayer(WorkshopBlocks.SINTERING_FURNACE.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.PRESS.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.ALEMBIC.getBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WorkshopBlocks.TEA_PLANT.getBlock(), RenderType.getCutout());
    }
}