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
import xyz.brassgoggledcoders.workshop.recipe.DryingBasinRecipe;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;
import xyz.brassgoggledcoders.workshop.recipe.SinteringFurnaceRecipe;

public class DryingBasinRecipeCategory implements IRecipeCategory<DryingBasinRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.DRYING_BASIN_SERIALIZER.get().getRecipeType().toString());

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
        return ID;
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
        return this.guiHelper.createBlankDrawable(160, 52);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.DRYING_BASIN.getBlock()));
    }

    @Override
    public void draw(DryingBasinRecipe recipe, double mouseX, double mouseY) {
        //Input
        slot.draw(34, 22);
        this.tank.draw(50, 0);
        //Output
        slot.draw(120, 22);
        //arrow.draw(24, 18);
    }

    @Override
    public void setIngredients(DryingBasinRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Lists.newArrayList(recipe.itemIn));
        iIngredients.setInput(VanillaTypes.FLUID, recipe.fluidIn);
        iIngredients.setOutput(VanillaTypes.ITEM, recipe.itemOut);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DryingBasinRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 32, 22);
        guiFluidStacks.init(1, true, 50, 0);
        guiItemStacks.init(2, false, 120, 22);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }
}
