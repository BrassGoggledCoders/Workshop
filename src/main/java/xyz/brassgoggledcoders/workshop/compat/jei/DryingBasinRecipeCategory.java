package xyz.brassgoggledcoders.workshop.compat.jei;

import com.google.common.collect.Lists;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.recipe.DryingBasinRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.DryingBasinTileEntity;

public class DryingBasinRecipeCategory implements IRecipeCategory<DryingBasinRecipe> {

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;
    private final IDrawable tank;

    public DryingBasinRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
        this.tank = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 0, 20, 58).build();
    }

    @Override
    public ResourceLocation getUid() {
        return DryingBasinTileEntity.ID;
    }

    @Override
    public Class<? extends DryingBasinRecipe> getRecipeClass() {
        return DryingBasinRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Drying Basin";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 54);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.DRYING_BASIN.getBlock()));
    }

    @Override
    public void draw(DryingBasinRecipe recipe, MatrixStack stack, double mouseX, double mouseY) {
        //Input
        slot.draw(stack, 0, 22);
        this.tank.draw(stack, 20, 0);
        //Output
        slot.draw(stack, 100, 22);
        arrow.draw(stack, 40, 20);
    }

    @Override
    public void setIngredients(DryingBasinRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Lists.newArrayList(recipe.itemIn));
        if (!recipe.fluidIn.isEmpty()) {
            iIngredients.setInput(VanillaTypes.FLUID, recipe.fluidIn);
        }
        iIngredients.setOutput(VanillaTypes.ITEM, recipe.itemOut);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DryingBasinRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 0, 22);
        guiFluidStacks.init(1, true, 24, 4, 12, 50, DryingBasinTileEntity.tankSize, true, null);
        guiItemStacks.init(2, false, 100, 22);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }
}
