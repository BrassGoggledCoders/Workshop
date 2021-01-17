package xyz.brassgoggledcoders.workshop.compat.jei;

import com.google.common.collect.Lists;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.PressTileEntity;

public class PressRecipeCategory implements IRecipeCategory<PressRecipe> {


    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public PressRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = WorkshopJEIPlugin.getDefaultArrow(guiHelper);
    }

    @Override
    public ResourceLocation getUid() {
        return PressTileEntity.ID;
    }

    @Override
    public Class<? extends PressRecipe> getRecipeClass() {
        return PressRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Press";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(70, 20);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.PRESS.getBlock()));
    }

    @Override
    public void draw(PressRecipe recipe, MatrixStack stack, double mouseX, double mouseY) {
        //Input
        slot.draw(stack, 0, 0);
        arrow.draw(stack, 20, 0);
    }

    @Override
    public void setIngredients(PressRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Lists.newArrayList(recipe.itemIn));
        iIngredients.setOutput(VanillaTypes.FLUID, recipe.fluidOut);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PressRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 0, 0);
        guiFluidStacks.init(1, false, 50, 0);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }
}
