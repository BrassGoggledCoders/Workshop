package xyz.brassgoggledcoders.workshop.jei;

import com.google.common.collect.Lists;
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
import xyz.brassgoggledcoders.workshop.recipe.MortarRecipe;
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.MortarTileEntity;

public class MortarRecipeCategory implements IRecipeCategory<MortarRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.MORTAR.toString());

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public MortarRecipeCategory(IGuiHelper guiHelper) {
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
    public Class<? extends MortarRecipe> getRecipeClass() {
        return MortarRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Mortar and Pestle";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 52);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.MORTAR.getBlock()));
    }

    @Override
    public void draw(MortarRecipe recipe, double mouseX, double mouseY) {
        //Input
        for(int i = 0; i < MortarTileEntity.inputSize; i++) {
            slot.draw(0, 22 + (i * 17));
        }
        //Output
        slot.draw(120, 22);
        //arrow.draw(24, 18);
    }

    @Override
    public void setIngredients(MortarRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Lists.newArrayList(recipe.input));
        iIngredients.setOutput(VanillaTypes.ITEM, recipe.output);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MortarRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        for(int i = 0; i < MortarTileEntity.inputSize; i++) {
            guiItemStacks.init(i, true, 0, 22 + (i * 17));
        }
        guiItemStacks.init(7, false, 120, 22);

        guiItemStacks.set(ingredients);
    }
}
