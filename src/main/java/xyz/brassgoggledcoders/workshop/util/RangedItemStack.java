package xyz.brassgoggledcoders.workshop.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hrznstudio.titanium.recipe.serializer.JSONSerializableDataHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.network.PacketBuffer;

import java.util.Iterator;
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
        stack.setCount(RandomValueRange.of(stackOutput.min, stackOutput.max).generateInt(random));
        return stack;
    }

    public static JsonElement serialize(RangedItemStack stack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("min", stack.min);
        jsonObject.addProperty("max", stack.max);
        jsonObject.addProperty("weight", stack.weight);
        jsonObject.add("stack", JSONSerializableDataHandler.writeItemStack(stack.stack));
        return jsonObject;
    }

    public static void write(PacketBuffer buf, RangedItemStack stack) {
        buf.writeItemStack(stack.stack);
        buf.writeInt(stack.min);
        buf.writeInt(stack.max);
        buf.writeInt(stack.weight);
    }

    public static RangedItemStack deserialize(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        return new RangedItemStack(JSONSerializableDataHandler.readItemStack(jsonObject.get("stack").getAsJsonObject()), jsonObject.get("min").getAsInt(), jsonObject.get("max").getAsInt(), jsonObject.get("weight").getAsInt());
    }

    public static RangedItemStack read(PacketBuffer buf) {
        return new RangedItemStack(buf.readItemStack(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static JsonElement serializeArr(RangedItemStack[] stacks) {
        JsonArray array = new JsonArray();
        for (RangedItemStack rStack : stacks) {
            array.add(JSONSerializableDataHandler.write(RangedItemStack.class, rStack));
        }
        return array;
    }

    public static void writeArr(PacketBuffer buf, RangedItemStack[] rangedItemStacks) {
        buf.writeInt(rangedItemStacks.length);
        for (RangedItemStack rangedItemStack : rangedItemStacks) {
            write(buf, rangedItemStack);
        }
    }

    public static RangedItemStack[] deserializeArr(JsonElement element) {
        RangedItemStack[] stacks = new RangedItemStack[element.getAsJsonArray().size()];
        int i = 0;
        Iterator<JsonElement> iterator;
        for (iterator = element.getAsJsonArray().iterator(); iterator.hasNext(); i++) {
            JsonElement jsonElement = iterator.next();
            stacks[i] = JSONSerializableDataHandler.read(RangedItemStack.class, jsonElement);
        }
        return stacks;
    }

    public static RangedItemStack[] readArr(PacketBuffer buf) {
        RangedItemStack[] stacks = new RangedItemStack[buf.readInt()];
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = RangedItemStack.read(buf);
        }
        return stacks;
    }
}
