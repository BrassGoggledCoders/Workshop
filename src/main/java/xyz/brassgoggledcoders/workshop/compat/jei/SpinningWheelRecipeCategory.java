package xyz.brassgoggledcoders.workshop.compat.jei;

import com.hrznstudio.titanium.Titanium;
import com.mojang.blaze3d.matrix.MatrixStack;
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
import xyz.brassgoggledcoders.workshop.recipe.SpinningWheelRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.SpinningWheelTileEntity;

import java.util.Arrays;

public class SpinningWheelRecipeCategory implements IRecipeCategory<SpinningWheelRecipe> {

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public SpinningWheelRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return SpinningWheelTileEntity.ID;
    }

    @Override
    public Class<SpinningWheelRecipe> getRecipeClass() {
        return SpinningWheelRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Spinning Wheel";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 52);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.SPINNING_WHEEL.getBlock()));
    }

    @Override
    public void draw(SpinningWheelRecipe recipe, MatrixStack stack, double mouseX, double mouseY) {
        for (int i = 0; i < 3; i++) {
            slot.draw(stack, 0, (i * 17));
        }
        slot.draw(stack, 50, 22);
        arrow.draw(stack, 24, 18);
    }

    @Override
    public void setIngredients(SpinningWheelRecipe spinningWheelRecipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Arrays.asList(spinningWheelRecipe.inputs));
        iIngredients.setOutput(VanillaTypes.ITEM, spinningWheelRecipe.output);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SpinningWheelRecipe spinningWheelRecipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        for (int i = 0; i < 3; i++) {
            if (i < ingredients.getInputs(VanillaTypes.ITEM).size()) {
                guiItemStacks.init(i, true, 0, (i * 17));
            }
        }
        guiItemStacks.init(4, false, 50, 22);

        guiItemStacks.set(ingredients);
    }
}
