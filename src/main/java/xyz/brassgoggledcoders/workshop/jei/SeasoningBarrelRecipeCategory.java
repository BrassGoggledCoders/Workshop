package xyz.brassgoggledcoders.workshop.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;

public class SeasoningBarrelRecipeCategory extends AbstractBarrelRecipeCategory<SeasoningBarrelRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.SEASONING_BARREL_SERIALIZER.get().getRecipeType().toString());

    public SeasoningBarrelRecipeCategory(IGuiHelper guiHelper) {
       super(guiHelper);
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
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.SEASONING_BARREL.getBlock()));
    }
}
