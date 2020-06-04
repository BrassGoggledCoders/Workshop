package xyz.brassgoggledcoders.workshop.api.impl;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.workshop.api.IDrinkableFluidBehaviour;

public class FoodFluidBehaviour implements IDrinkableFluidBehaviour {
    private final Food food;

    public FoodFluidBehaviour(Food food) {
        this.food = food;
    }

    @Override
    public void onFluidDrunk(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            ((PlayerEntity) entityLiving).getFoodStats().addStats(food.getHealing(), food.getSaturation());
        } else {
            entityLiving.heal(food.getHealing());
        }
        for (Pair<EffectInstance, Float> pair : food.getEffects()) {
            if (!worldIn.isRemote && pair.getLeft() != null && worldIn.rand.nextFloat() < pair.getRight()) {
                entityLiving.addPotionEffect(new EffectInstance(pair.getLeft()));
            }
        }
    }
}
