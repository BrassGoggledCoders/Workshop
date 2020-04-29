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
import xyz.brassgoggledcoders.workshop.tileentity.CollectorTileEntity;

import javax.annotation.Nonnull;

public class CollectorRecipe extends WorkshopRecipe {

    public TileEntityType<CollectorTileEntity> targetTileType;
    public Ingredient input;
    public ItemStack output;
    public int processingTime = 500;

    public CollectorRecipe(ResourceLocation resourceLocation, TileEntityType<CollectorTileEntity> targetType, Ingredient input, ItemStack output, int processingTime) {
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
    public ItemStack getCraftingResult(IInventory inv) {
        return output;
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
    @Nonnull
    public IRecipeType<?> getType() {
        return WorkshopRecipes.COLLECTOR_SERIALIZER.get().getRecipeType();
    }

    @Override
    public int getProcessingTime() {
        return processingTime;
    }
}
