package xyz.brassgoggledcoders.workshop.recipe;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import xyz.brassgoggledcoders.workshop.util.InventoryUtil;

import javax.annotation.Nonnull;

import static xyz.brassgoggledcoders.workshop.content.WorkshopRecipes.ALEMBIC_SERIALIZER;

public class AlembicRecipe extends SerializableRecipe implements IMachineRecipe {

    public Ingredient[] input = new Ingredient[0];
    public FluidStack output = FluidStack.EMPTY;
    public LootTable residue;
    public int processingTime = 0;

    public AlembicRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public AlembicRecipe(ResourceLocation name, Ingredient[] input, FluidStack output, LootTable residue, int cooldownTime) {
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
