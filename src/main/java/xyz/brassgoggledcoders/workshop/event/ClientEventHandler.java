package xyz.brassgoggledcoders.workshop.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopEffects;
import xyz.brassgoggledcoders.workshop.util.HUDRender;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID, value = Dist.CLIENT)
@SuppressWarnings("unused")
public class ClientEventHandler {
    //TODO Can we do this more efficiently?
    @SubscribeEvent
    public static void onRenderGameOverlayPostEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            HUDRender.render();
        }
    }
    @SubscribeEvent
    public static void onInput(InputUpdateEvent event) {
        if(event.getPlayer().isPotionActive(WorkshopEffects.DRUNK.get())) {
            event.getMovementInput().moveForward = -event.getMovementInput().moveForward;
            event.getMovementInput().moveStrafe = -event.getMovementInput().moveStrafe;
        }
    }
}