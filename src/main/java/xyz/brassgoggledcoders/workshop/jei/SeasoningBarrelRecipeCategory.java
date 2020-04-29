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
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;

import java.util.Collections;

public class SeasoningBarrelRecipeCategory implements IRecipeCategory<SeasoningBarrelRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.SEASONING_BARREL_SERIALIZER.get().getRecipeType().toString());

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public SeasoningBarrelRecipeCategory(IGuiHelper guiHelper) {
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
    public Class<? extends SeasoningBarrelRecipe> getRecipeClass() {
        return SeasoningBarrelRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Seasoning Barrel";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 52);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.SEASONING_BARREL.getBlock()));
    }

    @Override
    public void draw(SeasoningBarrelRecipe recipe, double mouseX, double mouseY) {
        slot.draw(29, 42);
        slot.draw(130, 42);
        //arrow.draw(24, 18);
    }

    @Override
    public void setIngredients(SeasoningBarrelRecipe seasoningBarrelRecipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(seasoningBarrelRecipe.itemIn));
        ingredients.setInput(VanillaTypes.FLUID, seasoningBarrelRecipe.fluidIn);
        ingredients.setOutput(VanillaTypes.ITEM, seasoningBarrelRecipe.itemOut);
        ingredients.setOutput(VanillaTypes.FLUID, seasoningBarrelRecipe.fluidOut);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SeasoningBarrelRecipe seasoningBarrelRecipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 29, 42);
        guiFluidStacks.init(1, true, 52, 20);
        guiItemStacks.init(2, false, 130, 42);
        guiFluidStacks.init(4, false, 105, 20);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }
}
