package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.SINTERING_FURNACE_SERIALIZER;

public class SinteringFurnaceRecipe extends WorkshopRecipe {

    public Ingredient powderIn = Ingredient.EMPTY;
    public Ingredient itemIn = Ingredient.EMPTY;
    public ItemStack itemOut = ItemStack.EMPTY;
    public int meltTime = 240;

    public SinteringFurnaceRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SinteringFurnaceRecipe(ResourceLocation resourceLocation, Ingredient powderIn, Ingredient itemIn, ItemStack itemOut, int processingTime) {
        this(resourceLocation);
        this.powderIn = powderIn;
        this.itemIn = itemIn;
        this.itemOut = itemOut;
        this.meltTime = processingTime;
    }

    public boolean matches(@Nonnull IItemHandler input, @Nonnull IItemHandler powder) {
        return itemIn.test(input.getStackInSlot(0)) && powderIn.test(powder.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return itemOut;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return itemOut;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SINTERING_FURNACE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return SINTERING_FURNACE_SERIALIZER.get().getRecipeType();
    }

    @Override
    public int getProcessingTime() {
        return meltTime;
    }
}
