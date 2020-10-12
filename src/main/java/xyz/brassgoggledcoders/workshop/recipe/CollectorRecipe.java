package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class CollectorRecipe extends WorkshopRecipe {

    public List<TileEntityType> targetTileType;
    public Ingredient input;
    public List<Pair<ItemStack, Integer>> outputs;
    public int processingTime = 500;

    public CollectorRecipe(ResourceLocation resourceLocation, List<TileEntityType> targetType, Ingredient input, List<Pair<ItemStack, Integer>> outputs, int processingTime) {
        this(resourceLocation);
        this.targetTileType = targetType;
        this.input = input;
        this.outputs = outputs;
        this.processingTime = processingTime;
    }

    public CollectorRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public List<Pair<ItemStack, Integer>> getOutputs() {
        return outputs;
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
        int weightSum = outputs.stream().mapToInt(Pair::getRight).sum();
        int randomInt = random.nextInt(weightSum);
        for (Pair<ItemStack, Integer> output : outputs) {
            if (randomInt < output.getRight()) {
                return output.getLeft();
            }
            else {
                randomInt -= output.getRight();
            }
        }
        return outputs.get(outputs.size() - 1).getLeft();
    }
}
