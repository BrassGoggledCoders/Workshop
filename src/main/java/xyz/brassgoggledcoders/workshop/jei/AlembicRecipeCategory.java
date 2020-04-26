package xyz.brassgoggledcoders.workshop.jei;

import com.hrznstudio.titanium.Titanium;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.recipe.SpinningWheelRecipe;

import java.util.Arrays;

public class AlembicRecipeCategory implements IRecipeCategory<AlembicRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.ALEMBIC.toString());

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public AlembicRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends AlembicRecipe> getRecipeClass() {
        return AlembicRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Alembic";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 52);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.ALEMBIC.getBlock()));
    }

    @Override
    public void draw(AlembicRecipe recipe, double mouseX, double mouseY) {
        //Inputs
        for (int i = 0; i < 3; i++) {
            slot.draw(34, 25 + (i * 17));
        }
        //Container
        slot.draw(56, 43);
        //Output
        slot.draw(102, 44);
        //Residue
        for(int i = 0; i < 3; i++) {
            slot.draw(125, 25 + (i * 17));
        }
        //arrow.draw(24, 18);
    }

    @Override
    public void setIngredients(AlembicRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Arrays.asList(recipe.input));
        iIngredients.setOutput(VanillaTypes.FLUID, recipe.output);
        //iIngredients.setOutput(VanillaTypes.ITEM, recipe.residue);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlembicRecipe spinningWheelRecipe, IIngredients ingredients) {

    }
}
