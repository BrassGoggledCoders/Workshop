package xyz.brassgoggledcoders.workshop.jei;

import com.hrznstudio.titanium.Titanium;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.AbstractBarrelRecipe;
import xyz.brassgoggledcoders.workshop.recipe.MoltenChamberRecipe;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;

import java.util.Collections;

public class MoltenChamberRecipeCategory extends AbstractBarrelRecipeCategory {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.MOLTEN_CHAMBER_SERIALIZER.get().getRecipeType().toString());

    public MoltenChamberRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends MoltenChamberRecipe> getRecipeClass() {
        return MoltenChamberRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Molten Reaction Chamber";
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.MOLTEN_CHAMBER.getBlock()));
    }


}
