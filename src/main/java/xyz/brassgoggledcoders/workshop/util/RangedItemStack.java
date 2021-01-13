package xyz.brassgoggledcoders.workshop.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class RangedItemStack {
    public final ItemStack stack;
    public final int min;
    public final int max;
    public final int weight;

    public RangedItemStack(ItemStack stack, int min, int max, int weight) {
        this.stack = stack;
        this.min = min;
        this.max = max;
        this.weight = weight;
    }

    public RangedItemStack(Item item, int min, int max) {
        this(new ItemStack(item), min, max);
    }

    public RangedItemStack(ItemStack stack, int min, int max) {
        this(stack, min, max, 1);
    }

    public RangedItemStack(ItemStack stack) {
        this(stack, 1, 1);
    }

    public static ItemStack getOutput(Random random, RangedItemStack stackOutput) {
        ItemStack stack = stackOutput.stack;
        stack.setCount(Math.max(stackOutput.min, random.nextInt(stackOutput.max)));
        return stack;
    }
}
