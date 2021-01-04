package xyz.brassgoggledcoders.workshop.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

public class ScrapBagItem extends Item {
    public ScrapBagItem(Properties properties) {
        super(properties);
        DispenserBlock.registerDispenseBehavior(this, new DispenseScrapBagBehaviour());
    }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            getLoot((ServerWorld) worldIn, playerIn, null).forEach(stack -> ItemHandlerHelper.insertItem(new PlayerInvWrapper(playerIn.inventory), stack, false));
            playerIn.getHeldItem(handIn).shrink(1);
            playerIn.addPotionEffect(new EffectInstance(WorkshopEffects.STINKY.get(), 20 * 60 * 5));
            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public static List<ItemStack> getLoot(ServerWorld worldIn, @Nullable PlayerEntity playerIn, @Nullable BlockPos position) {
        if(position == null && playerIn != null) {
            position = playerIn.getPosition();
        }
        if(position != null) {
            return worldIn.getServer()
                    .getLootTableManager()
                    .getLootTableFromLocation( new ResourceLocation(Workshop.MOD_ID, "gameplay/scrap_bag"))
                    .generate(new LootContext.Builder(worldIn)
                            .withParameter(LootParameters.field_237457_g_, new Vector3d(position.getX(), position.getY(), position.getZ()))
                            .withNullableParameter(LootParameters.THIS_ENTITY, playerIn)
                            //Has to have pos, entity is optional
                            .build(LootParameterSets.COMMAND));
        }
        return Collections.emptyList();
    }
}
