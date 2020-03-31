package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import com.hrznstudio.titanium.util.ArrayUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.stream.IntStream;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.ALEMBIC_SERIALIZER;

public class AlembicRecipe extends SerializableRecipe implements IMachineRecipe {

    public Ingredient[] input;
    public FluidStack output;
    public ItemStack[] residue;
    public int processingTime;

    public AlembicRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public AlembicRecipe(ResourceLocation name, Ingredient[] input, FluidStack output, ItemStack[] residue, int cooldownTime) {
        this(name);
        this.input = input;
        this.output = output;
        this.residue = residue;
        this.processingTime = cooldownTime;
    }

    public boolean matches(@Nonnull IItemHandler handler) {
        //For each ingredient in the input list check if any of the slots in the handler match the Ingredient predicate
        return Arrays.stream(input) //Stream the list
                //Check that every Ingredient in the list matches against one of the slots
                .allMatch(ingredient ->
                        //Stream the slots for each ingredient
                        IntStream.range(0, handler.getSlots() - 1)
                        //Get the stack in each slot
                        .mapToObj(slotIndex -> handler.getStackInSlot(slotIndex))
                        //Filter out empties
                        .filter(stack -> !stack.isEmpty())
                        //Check if any of them match the Ingredient
                        .anyMatch(stack -> ingredient.test(stack)));
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
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
