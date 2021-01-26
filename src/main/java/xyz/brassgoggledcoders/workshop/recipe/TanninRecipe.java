package xyz.brassgoggledcoders.workshop.recipe;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;

public class TanninRecipe extends SpecialRecipe {
    public TanninRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack armor = ItemStack.EMPTY;
        boolean hasTannin = false;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() instanceof DyeableArmorItem) {
                    armor = itemstack1;
                } else if (itemstack1.getItem() == WorkshopItems.TANNIN.get()) {
                    hasTannin = true;
                }
            }
        }

        return !armor.isEmpty() && hasTannin;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inv.getStackInSlot(j);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() instanceof DyeableArmorItem) {
                    itemstack1.addEnchantment(Enchantments.UNBREAKING, 1);
                    itemstack = itemstack1;
                }
            }
        }

        return itemstack;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return WorkshopRecipes.TANNIN_SERIALIZER.get();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }
}