package xyz.brassgoggledcoders.workshop.content;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
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
import xyz.brassgoggledcoders.workshop.api.capabilities.*;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID)
@SuppressWarnings("unused")
public class WorkshopCapabilities {
    @CapabilityInject(CollectorTarget.class)
    public static Capability<CollectorTarget> COLLECTOR_TARGET;

    @SubscribeEvent
    public void preInit(FMLCommonSetupEvent evt) {
        CapabilityManager.INSTANCE.register(CollectorTarget.class, new NOPStorage<>(),
                DefaultCollectorTarget::new);
    }

    @SubscribeEvent
    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        Item item = event.getObject().getItem();
        if (item == Items.GLASS_BOTTLE || item == Items.HONEY_BOTTLE ||
                (item == Items.POTION && Potions.WATER.equals(PotionUtils.getPotionFromItem(event.getObject())))) {
            event.addCapability(new ResourceLocation(Workshop.MOD_ID, "fluid_handler_item"),
                    new BottleCapabilityProvider(event.getObject()));
        }
    }

    @SubscribeEvent
    public static void attachTileCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
        if (event.getObject() instanceof FurnaceTileEntity) {
            event.addCapability(new ResourceLocation(Workshop.MOD_ID, "collector_target"),
                    new FurnaceCapabilityProvider((FurnaceTileEntity) event.getObject()));
        }
    }
}
