package xyz.brassgoggledcoders.workshop.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.capabilities.BottleCapabilityProvider;
import xyz.brassgoggledcoders.workshop.content.WorkshopFluids;

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
        PlayerEntity playerentity = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;
        if (playerentity instanceof ServerPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)playerentity, stack);
        }

        if (!worldIn.isRemote) {
            //TODO give the fluid control over this
            if(WorkshopFluids.TEA.get().equals(this.getFluid())) {
                Potions.SWIFTNESS.getEffects().forEach(effect -> effect.getPotion().affectEntity(playerentity, playerentity, entityLiving, effect.getAmplifier(), 1.0D));
            }
        }

        //TODO Refactor
        if (playerentity != null) {
            playerentity.addStat(Stats.ITEM_USED.get(this));
            if (!playerentity.abilities.isCreativeMode) {
                stack.shrink(1);
            }
        }

        if (playerentity == null || !playerentity.abilities.isCreativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerentity != null) {
                playerentity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    //Copy of private method in BucketItem
    public ItemStack fillBucket(ItemStack emptyBuckets, PlayerEntity player, Item fullBucket) {
        if (player.abilities.isCreativeMode) {
            return emptyBuckets;
        } else {
            emptyBuckets.shrink(1);
            if (emptyBuckets.isEmpty()) {
                return new ItemStack(fullBucket);
            } else {
                if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket))) {
                    player.dropItem(new ItemStack(fullBucket), false);
                }

                return emptyBuckets;
            }
        }
    }
}
