package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.ALEMBIC_SERIALIZER;

public class AlembicRecipe extends WorkshopRecipe {

    public Ingredient[] input = new Ingredient[0];
    public FluidStack output = FluidStack.EMPTY;
    public RangedItemStack[] residue;
    public int processingTime = 0;

    public AlembicRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public AlembicRecipe(ResourceLocation name, Ingredient[] input, FluidStack output, RangedItemStack[] residue, int cooldownTime) {
        this(name);
        this.input = input;
        this.output = output;
        this.residue = residue;
        this.processingTime = cooldownTime;
    }

    public boolean matches(@Nonnull IItemHandler handler) {
        //For each ingredient in the input list check if any of the slots in the handler match the Ingredient predicate
        return InventoryUtil.inventoryHasIngredients(handler, input);
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return ALEMBIC_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ALEMBIC_SERIALIZER.get().getRecipeType();
    }

    @Override
    public int getProcessingTime() {
        return this.processingTime;
    }
}
