package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.api.DefaultCollectorTarget;
import xyz.brassgoggledcoders.workshop.api.FurnaceCapabilityProvider;
import xyz.brassgoggledcoders.workshop.api.ICollectorTarget;
import xyz.brassgoggledcoders.workshop.api.NOPStorage;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID)
public class WorkshopCapabilities {
    @CapabilityInject(ICollectorTarget.class)
    public static Capability<ICollectorTarget> COLLECTOR_TARGET;

    @SubscribeEvent
    public void preInit(FMLCommonSetupEvent evt) {
        CapabilityManager.INSTANCE.register(ICollectorTarget.class, new NOPStorage<>(),
                DefaultCollectorTarget::new);
    }

    @SubscribeEvent
    public static void attachTileCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
        if(event.getObject() instanceof FurnaceTileEntity) {
            event.addCapability(new ResourceLocation(Workshop.MOD_ID, "collector_target"),
                    new FurnaceCapabilityProvider((FurnaceTileEntity) event.getObject()));
            Workshop.LOGGER.warn("Attached cap to furnace");
        }
    }
}
