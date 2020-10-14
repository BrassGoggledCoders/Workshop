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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollectorRecipe extends WorkshopRecipe {

    public WeightedOutputList weightedOutputList;
    public TileEntityList targetTileTypes;
    public Ingredient input;
    public int processingTime = 500;

    public CollectorRecipe(ResourceLocation resourceLocation, TileEntityList targetType, Ingredient input, List<Pair<ItemStack, Integer>> outputs, int processingTime) {
        this(resourceLocation);
        this.targetTileTypes = targetType;
        this.input = input;
        this.weightedOutputList = new WeightedOutputList(outputs);
        this.processingTime = processingTime;
    }

    public CollectorRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public List<Pair<ItemStack, Integer>> getOutputs() {
        return weightedOutputList.outputs;
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
        int weightSum = weightedOutputList.outputs.stream().mapToInt(Pair::getRight).sum();
        int randomInt = random.nextInt(weightSum);
        for (Pair<ItemStack, Integer> output : weightedOutputList.outputs) {
            if (randomInt < output.getRight()) {
                return output.getLeft();
            }
            else {
                randomInt -= output.getRight();
            }
        }
        return weightedOutputList.outputs.get(weightedOutputList.outputs.size() - 1).getLeft();
    }

    public static class WeightedOutputList {
        private List<Pair<ItemStack, Integer>> outputs;

        public WeightedOutputList(List<Pair<ItemStack, Integer>> outputs) {
            this.outputs = outputs;
        }

        public WeightedOutputList(){
            this.outputs = new ArrayList<>();
        }

        public List<Pair<ItemStack, Integer>> getOutputs() {
            return outputs;
        }
    }

    public static class TileEntityList {
        private final List<TileEntityType> tileEntityTypes;

        public TileEntityList() {
            tileEntityTypes = new ArrayList<>();
        }

        public List<TileEntityType> getTileEntityTypes() {
            return tileEntityTypes;
        }
    }
}
