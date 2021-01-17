package xyz.brassgoggledcoders.workshop.compat.jei;

import com.hrznstudio.titanium.Titanium;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.recipe.AbstractBarrelRecipe;

import java.util.Collections;

public abstract class AbstractBarrelRecipeCategory<T extends AbstractBarrelRecipe> implements IRecipeCategory<T> {

    protected final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public AbstractBarrelRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 30);
    }

    @Override
    public void draw(T recipe, MatrixStack stack, double mouseX, double mouseY) {
        slot.draw(stack, 0, 0);
        slot.draw(stack, 90, 0);
        arrow.draw(stack, 40, 0);
    }

    @Override
    public void setIngredients(T recipe, IIngredients ingredients) {
        if (!Ingredient.EMPTY.equals(recipe.itemIn)) {
            ingredients.setInputIngredients(Collections.singletonList(recipe.itemIn));
        }
        if (!recipe.fluidIn.isEmpty()) {
            ingredients.setInput(VanillaTypes.FLUID, recipe.fluidIn);
        }
        if (!recipe.itemOut.isEmpty()) {
            ingredients.setOutput(VanillaTypes.ITEM, recipe.itemOut);
        }
        if (!recipe.fluidOut.isEmpty()) {
            ingredients.setOutput(VanillaTypes.FLUID, recipe.fluidOut);
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 0, 0);
        guiFluidStacks.init(1, true, 20, 1);
        guiFluidStacks.init(2, false, 70, 1);
        guiItemStacks.init(3, false, 90, 0);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }
}
