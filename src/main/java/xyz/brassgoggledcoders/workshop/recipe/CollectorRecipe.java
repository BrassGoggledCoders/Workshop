package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CollectorRecipe extends WorkshopRecipe {

    public TileEntityType<?>[] targetTileTypes;
    public Ingredient input;
    public RangedItemStack[] outputs;
    public int processingTime = 500;

    public CollectorRecipe(ResourceLocation resourceLocation, Ingredient input, int processingTime, RangedItemStack[] outputs, TileEntityType<?>... targetType) {
        this(resourceLocation);
        this.targetTileTypes = targetType;
        this.input = input;
        this.outputs = outputs;
        this.processingTime = processingTime;
    }

    public CollectorRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public List<RangedItemStack> getOutputs() {
        return Arrays.asList(outputs);
    }

    public List<TileEntityType<?>> getTileEntityTypes() {
        return Arrays.asList(targetTileTypes);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return WorkshopRecipes.COLLECTOR_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return WorkshopRecipes.COLLECTOR_SERIALIZER.get().getRecipeType();
    }

    @Override
    public int getProcessingTime() {
        return processingTime;
    }

    public ItemStack getRecipeOutput(Random random) {
        /* 1. Calculate sum of weights
            2. Pick a random number greater than zero and less than sum
            3. Subtract each items weight until we find the one where randInt is less than the item's weight
         */
        if(getOutputs().size() == 1) {
            return RangedItemStack.getOutput(random, getOutputs().get(0));
        } else {
            int weightSum = getOutputs().stream().mapToInt(stack -> stack.weight).sum();
            int randomInt = random.nextInt(weightSum);
            for (RangedItemStack rStack : getOutputs()) {
                if (randomInt < rStack.weight) {
                    return RangedItemStack.getOutput(random, rStack);
                } else {
                    randomInt -= rStack.weight;
                }
            }
        }
        //Don't think this can ever happen? TODO: Check!
        return ItemStack.EMPTY;
    }
}
