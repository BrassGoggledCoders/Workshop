package xyz.brassgoggledcoders.workshop.util;

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InventoryUtil {

    public static final String ITEM_INPUT = "item_input";
    public static final DyeColor ITEM_INPUT_COLOR = DyeColor.BLUE;
    public static final String ITEM_OUTPUT = "item_output";
    public static final DyeColor ITEM_OUTPUT_COLOR = DyeColor.RED;

    public static Stream<ItemStack> getItemStackStream(IItemHandler inventory) {
        //Get slot indexes
        return IntStream.range(0, inventory.getSlots())
                //Map to each slot
                .mapToObj(inventory::getStackInSlot);
    }

    public static boolean anySlotsHaveItems(IItemHandler inventory) {
        return getItemStackStream(inventory)
                .anyMatch(stack -> !stack.isEmpty());
    }

    public static boolean inventoryHasIngredients(IItemHandler target, Ingredient[] required) {
        return Arrays.stream(required) //Stream the list
                //Check every Ingredient in the list matches against one of the slots
                .allMatch(ingredient ->
                        //Stream the stacks
                        getItemStackStream(target)
                                //Filter out empties
                                .filter(stack -> !stack.isEmpty())
                                //Check if any of them match the Ingredient
                                .anyMatch(ingredient));
    }
}
