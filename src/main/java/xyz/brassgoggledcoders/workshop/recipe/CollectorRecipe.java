package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;

public class CollectorRecipe extends SerializableRecipe implements IMachineRecipe {

    public TileEntityType targetTileType;
    public Ingredient input;
    public ItemStack output;
    public int processingTime;

    public CollectorRecipe(ResourceLocation resourceLocation, TileEntityType targetType, Ingredient input, ItemStack output, int processingTime) {
        this(resourceLocation);
        this.targetTileType = targetType;
        this.input = input;
        this.output = output;
        this.processingTime = processingTime;
    }

    public CollectorRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return output;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return WorkshopRecipes.COLLECTOR_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return WorkshopRecipes.COLLECTOR;
    }

    @Override
    public int getProcessingTime() {
        return processingTime;
    }
}
