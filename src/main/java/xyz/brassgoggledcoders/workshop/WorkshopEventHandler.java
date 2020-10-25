package xyz.brassgoggledcoders.workshop;

import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.workshop.content.WorkshopEffects;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.util.HUDRender;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID)
@SuppressWarnings("unused")
public class WorkshopEventHandler {

    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getPlayer().getActivePotionEffect(WorkshopEffects.STINKY.get()) != null && event.getTarget() instanceof IMerchant) {
            if (event.getTarget() instanceof AbstractVillagerEntity) {
                ((AbstractVillagerEntity) event.getTarget()).setShakeHeadTicks(20);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {
        String prefix = "minecraft:";
        String name = event.getName().toString();
        if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            if ("blocks/grass".equals(file)) {
                event.getTable().addPool(getInjectPool(file));
            }
        }
    }

    @SubscribeEvent
    public static void anvilChange(AnvilUpdateEvent event) {
        if (WorkshopItems.LEATHER_CORDAGE.get().equals(event.getRight().getItem())) {
            if (event.getLeft().getItem() instanceof DyeableArmorItem && event.getLeft().isDamaged()) {
                event.setMaterialCost(1);
                ItemStack stack = event.getLeft().copy();
                stack.setDamage(stack.getDamage() - 20);
                event.setOutput(stack);
            }
        }
    }

    //Ta Botania
    public static LootPool getInjectPool(String entryName) {
        return LootPool.builder()
                .addEntry(getInjectEntry(entryName))
                .bonusRolls(0, 1)
                .name(Workshop.MOD_ID + "_inject")
                .build();
    }

    private static LootEntry.Builder<?> getInjectEntry(String name) {
        ResourceLocation table = new ResourceLocation(Workshop.MOD_ID, "inject/" + name);
        return TableLootEntry.builder(table)
                .weight(1);
    }

    //TODO Can we do this more efficiently?
    @SubscribeEvent
    public static void onRenderGameOverlayPostEvent(RenderGameOverlayEvent.Post event) {
        RenderGameOverlayEvent.ElementType type = event.getType();

        if (type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        HUDRender.render();
    }
}
