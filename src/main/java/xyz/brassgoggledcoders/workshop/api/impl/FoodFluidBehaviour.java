package xyz.brassgoggledcoders.workshop.api.impl;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
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
            if (!worldIn.isRemote && pair.getFirst() != null && worldIn.rand.nextFloat() < pair.getSecond()) {
                entityLiving.addPotionEffect(new EffectInstance(pair.getFirst()));
            }
        }
    }
}
