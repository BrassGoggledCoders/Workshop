package xyz.brassgoggledcoders.workshop.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.api.WorkshopAPI;
import xyz.brassgoggledcoders.workshop.capabilities.BottleCapabilityProvider;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BottleItem extends BucketItem {

    public BottleItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    protected ItemStack emptyBucket(ItemStack stack, PlayerEntity player) {
        return !player.abilities.isCreativeMode ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
        return new BottleCapabilityProvider(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;

            if (playerEntity instanceof ServerPlayerEntity) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
            }

            if (!worldIn.isRemote && WorkshopAPI.getDrinkableFluidBehaviors().containsKey(this.getFluid())) {
                WorkshopAPI.getDrinkableFluidBehaviors().get(this.getFluid()).onFluidDrunk(stack, worldIn, entityLiving);
                playerEntity.addStat(Stats.ITEM_USED.get(this));
                if (!playerEntity.abilities.isCreativeMode) {
                    stack.shrink(1);
                    playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
                    if (stack.isEmpty()) {
                        return new ItemStack(Items.GLASS_BOTTLE);
                    }
                }
            }
        }
        return stack;
    }
}
