package xyz.brassgoggledcoders.workshop.jei;

import com.google.common.collect.Lists;
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
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.MortarRecipe;
import xyz.brassgoggledcoders.workshop.recipe.PressRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.MortarTileEntity;

public class PressRecipeCategory implements IRecipeCategory<PressRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.PRESS_SERIALIZER.get().getRecipeType().toString());

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
        return ID;
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
    public void draw(PressRecipe recipe, double mouseX, double mouseY) {
        //Input
        slot.draw(0, 0);
        arrow.draw(20, 0);
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
