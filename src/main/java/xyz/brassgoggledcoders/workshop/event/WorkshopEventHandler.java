package xyz.brassgoggledcoders.workshop.event;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.api.IStackingEffect;
import xyz.brassgoggledcoders.workshop.content.WorkshopEffects;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Workshop.MOD_ID)
@SuppressWarnings("unused")
public class WorkshopEventHandler {

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if(RayTraceResult.Type.ENTITY.equals(event.getRayTraceResult().getType())) {
            Entity hit = ((EntityRayTraceResult)event.getRayTraceResult()).getEntity();
            if(hit instanceof LivingEntity && ((LivingEntity) hit).isPotionActive(WorkshopEffects.WIRED.get())) {
                event.setCanceled(true);
                Random random = ((LivingEntity)hit).getRNG();
                if(random.nextBoolean()) {
                    ((LivingEntity) hit).attemptTeleport(hit.getPosX() + random.nextDouble(), hit.getPosY(), hit.getPosZ() + random.nextDouble(), false);
                }
                else {
                    ((LivingEntity) hit).attemptTeleport(hit.getPosX() - random.nextDouble(), hit.getPosY(), hit.getPosZ() - random.nextDouble(), false);
                }
                //Did you just catch a projectile out of the air? Yes. You're that wired.
                if(hit instanceof PlayerEntity && event.getEntity() instanceof ArrowEntity && random.nextInt(1000) == 0) {
                    ((PlayerEntity) hit).inventory.addItemStackToInventory(((ArrowEntity)event.getEntity()).getArrowStack());
                    event.getEntity().remove();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTakeDamage(LivingHurtEvent event) {
        if(event.getEntityLiving().isPotionActive(WorkshopEffects.MELLOW.get())) {
            if(event.getSource().getImmediateSource() instanceof LivingEntity) {
                if(CreatureAttribute.ARTHROPOD.equals(((LivingEntity) event.getSource().getImmediateSource()).getCreatureAttribute())) {
                    event.setAmount(event.getAmount() * 0.5F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        //FIXME Hook into food change event if/when this becomes possible
        if(event.getEntityLiving() instanceof PlayerEntity && event.getItem().isFood() && event.getEntityLiving().isPotionActive(WorkshopEffects.INEBRIATED.get())) {
            //Add saturation from food again, effectively doubling it
            FoodStats foodStats = ((PlayerEntity) event.getEntityLiving()).getFoodStats();
            foodStats.addStats(0,event.getItem().getItem().getFood().getSaturation());
        }
    }

    @SubscribeEvent
    public static void xpEvent(PlayerXpEvent.XpChange event) {
        if(event.getPlayer().isPotionActive(WorkshopEffects.INEBRIATED.get())) {
            event.setAmount((int) Math.floor(event.getAmount() * 0.7));
        }
    }

    @SubscribeEvent
    public static void wakeUp(PlayerWakeUpEvent event) {
        event.getPlayer().removePotionEffect(WorkshopEffects.DRUNK.get());
    }

    @SubscribeEvent
    public static void trySleep(PlayerSleepInBedEvent event) {
        if(event.getPlayer().isPotionActive(WorkshopEffects.WIRED.get())) {
            event.setResult(PlayerEntity.SleepResult.NOT_POSSIBLE_NOW);
        }
    }

    @SubscribeEvent
    public static void potionCheck(PotionEvent.PotionAddedEvent event) {
        EffectInstance potionEffect = event.getOldPotionEffect();
        if(potionEffect != null) {
            Effect potion = potionEffect.getPotion();
            if (potion instanceof IStackingEffect && event.getEntityLiving().isPotionActive(potion)) {
                if (potionEffect.getAmplifier() < (((IStackingEffect) potion).getMaxLevel() - 1)) {
                    event.getPotionEffect().combine(new EffectInstance(potion, potionEffect.getDuration(), potionEffect.getAmplifier() + 1));
                } else {
                    event.getEntityLiving().removePotionEffect(potion);
                    event.getEntityLiving().addPotionEffect(((IStackingEffect) potion).getPostMaxEffect());
                }
            }
        }
    }

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
}
