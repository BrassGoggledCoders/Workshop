package xyz.brassgoggledcoders.workshop.item;

import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class DispenseScrapBagBehaviour extends DefaultDispenseItemBehavior {
    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        if (!source.getWorld().isRemote() && WorkshopItems.SCRAP_BAG.get().equals(stack.getItem())) {
            List<ItemStack> list = ScrapBagItem.getLoot(source.getWorld(), null, source.getBlockPos());
            list.forEach(istack -> super.dispenseStack(source, istack));
            stack.shrink(1);
            return stack;
        }
        return super.dispenseStack(source, stack);
    }
}
