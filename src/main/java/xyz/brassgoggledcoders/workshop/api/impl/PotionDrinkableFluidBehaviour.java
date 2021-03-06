package xyz.brassgoggledcoders.workshop.api.impl;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.api.IDrinkableFluidBehaviour;

public class PotionDrinkableFluidBehaviour implements IDrinkableFluidBehaviour {

    protected final EffectInstance[] effectinstances;

    public PotionDrinkableFluidBehaviour(EffectInstance... effectinstances) {
        this.effectinstances = effectinstances;
    }

    @Override
    public void onFluidDrunk(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        for (EffectInstance effectinstance : effectinstances) {
            if (effectinstance.getPotion().isInstant()) {
                effectinstance.getPotion().affectEntity(entityLiving, entityLiving, entityLiving, effectinstance.getAmplifier(), 1.0D);
            } else {
                entityLiving.addPotionEffect(new EffectInstance(effectinstance));
            }
        }
    }

}
