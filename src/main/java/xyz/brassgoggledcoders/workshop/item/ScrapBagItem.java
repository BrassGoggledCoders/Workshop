package xyz.brassgoggledcoders.workshop.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import xyz.brassgoggledcoders.workshop.content.WorkshopEffects;
import xyz.brassgoggledcoders.workshop.datagen.loot.WorkshopGiftLootTables;

import java.util.List;

public class ScrapBagItem extends Item {
    public ScrapBagItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            List<ItemStack> list =
                    worldIn.getServer()
                            .getLootTableManager()
                            .getLootTableFromLocation(WorkshopGiftLootTables.SCRAP_BAG)
                            .generate(new LootContext.Builder((ServerWorld) worldIn)
                    .withParameter(LootParameters.POSITION, playerIn.getPosition()).withParameter(LootParameters.THIS_ENTITY, playerIn)
                    .build(LootParameterSets.GIFT));
            list.forEach(stack -> ItemHandlerHelper.insertItem(new PlayerInvWrapper(playerIn.inventory), stack, false));
            playerIn.getHeldItem(handIn).shrink(1);
            playerIn.addPotionEffect(new EffectInstance(WorkshopEffects.STINKY.get(), 20 * 60 * 5));
            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
