package xyz.brassgoggledcoders.workshop.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDrinkableFluidBehaviour {
    void onFluidDrunk(ItemStack stack, World worldIn, LivingEntity entityLiving);
}
