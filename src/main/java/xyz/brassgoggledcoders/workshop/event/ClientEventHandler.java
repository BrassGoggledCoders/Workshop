package xyz.brassgoggledcoders.workshop.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.util.HUDRender;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    //TODO Can we do this more efficiently?
    @SubscribeEvent
    public static void onRenderGameOverlayPostEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            HUDRender.render();
        }
    }
}