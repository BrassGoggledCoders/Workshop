package xyz.brassgoggledcoders.workshop.compat.jei;

import com.google.common.collect.Lists;
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
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.SinteringFurnaceTileEntity;

public class SinteringFurnaceRecipeCategory implements IRecipeCategory<SinteringFurnaceRecipe> {

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public SinteringFurnaceRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return SinteringFurnaceTileEntity.ID;
    }

    @Override
    public Class<? extends SinteringFurnaceRecipe> getRecipeClass() {
        return SinteringFurnaceRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Sintering Furnace";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 52);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.SINTERING_FURNACE.getBlock()));
    }

    @Override
    public void draw(SinteringFurnaceRecipe recipe, MatrixStack stack, double mouseX, double mouseY) {
        //Input
        slot.draw(stack, 34, 22);
        //Powder
        slot.draw(stack, 70, 0);
        slot.draw(stack, 87, 0);
        //Output
        slot.draw(stack, 120, 22);
        //arrow.draw(24, 18);
    }

    @Override
    public void setIngredients(SinteringFurnaceRecipe sinteringFurnaceRecipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Lists.newArrayList(sinteringFurnaceRecipe.itemIn, sinteringFurnaceRecipe.powderIn));
        iIngredients.setOutput(VanillaTypes.ITEM, sinteringFurnaceRecipe.itemOut);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SinteringFurnaceRecipe sinteringFurnaceRecipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 34, 22);
        guiItemStacks.init(1, true, 70, 0);
        guiItemStacks.init(2, false, 120, 22);

        guiItemStacks.set(ingredients);
    }
}
