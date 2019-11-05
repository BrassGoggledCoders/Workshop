package xyz.brassgoggledcoders.workshop.recipes;

import com.hrznstudio.titanium.recipe.serializer.GenericSerializer;
import com.hrznstudio.titanium.recipe.serializer.SerializableRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

import static xyz.brassgoggledcoders.workshop.Workshop.MOD_ID;

public class SinteringFurnaceRecipe extends SerializableRecipe {

    public static GenericSerializer<SinteringFurnaceRecipe> SERIALIZER = new GenericSerializer<>(new ResourceLocation(MOD_ID, "sinteringfurnace"), SinteringFurnaceRecipe.class);
    public static List<SinteringFurnaceRecipe> RECIPES = new ArrayList<>();

    public ItemStack powder;
    public ItemStack targetMaterial;
    public ItemStack output;
    public int meltTime;

    public SinteringFurnaceRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public SinteringFurnaceRecipe(ResourceLocation resourceLocation, ItemStack powder, ItemStack targetMaterial, ItemStack output, int meltTime) {
        super(resourceLocation);
        this.powder = powder;
        this.targetMaterial = targetMaterial;
        this.output = output;
        this.meltTime = meltTime;
        RECIPES.add(this);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    public boolean matches(IItemHandler powderIn, IItemHandler targetMaterialIn) {
        return this.powder.isItemEqual(powderIn.getStackInSlot(0)) && this.targetMaterial.isItemEqual(targetMaterialIn.getStackInSlot(0));
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
        return output;
    }

    @Override
    public GenericSerializer<? extends SerializableRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return SERIALIZER.getRecipeType();
    }
}
