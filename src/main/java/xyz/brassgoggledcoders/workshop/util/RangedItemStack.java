package xyz.brassgoggledcoders.workshop.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class RangedItemStack {
    public final ItemStack stack;
    public final int min;
    public final int max;

    public RangedItemStack(Item item, int min, int max) {
        this(new ItemStack(item), min, max);
    }

    public RangedItemStack(ItemStack stack, int min, int max) {
        this.stack = stack;
        this.min = min;
        this.max = max;
    }

    public static ItemStack getOutput(Random random, RangedItemStack stackOutput) {
        ItemStack stack = stackOutput.stack;
        stack.setCount(Math.max(stackOutput.min, random.nextInt(stackOutput.max)));
        return stack;
    }
}
