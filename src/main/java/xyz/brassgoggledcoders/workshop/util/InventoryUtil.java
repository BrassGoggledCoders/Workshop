package xyz.brassgoggledcoders.workshop.util;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;
import java.util.stream.IntStream;

public class InventoryUtil {
    public static boolean anySlotsHaveItems(IItemHandler inventory) {
        return IntStream.range(0, inventory.getSlots())
                .mapToObj(inventory::getStackInSlot)
                .anyMatch(stack -> !stack.isEmpty());
    }

    public static boolean inventoryHasIngredients(IItemHandler target, Ingredient[] required) {
        return Arrays.stream(required) //Stream the list
                //Check that every Ingredient in the list matches against one of the slots
                .allMatch(ingredient ->
                        //Stream the slots for each ingredient
                        IntStream.range(0, target.getSlots())
                                //Get the stack in each slot
                                .mapToObj(target::getStackInSlot)
                                //Filter out empties
                                .filter(stack -> !stack.isEmpty())
                                //Check if any of them match the Ingredient
                                .anyMatch(ingredient::test));
    }
}
