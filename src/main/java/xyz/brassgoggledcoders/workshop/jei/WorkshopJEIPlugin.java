package xyz.brassgoggledcoders.workshop.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.recipe.MortarRecipe;

@JeiPlugin
@SuppressWarnings("unused")
public class WorkshopJEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new AlembicRecipeCategory(guiHelper),
                new SpinningWheelRecipeCategory(guiHelper),
                new SeasoningBarrelRecipeCategory(guiHelper),
                new MoltenChamberRecipeCategory(guiHelper),
                new MortarRecipeCategory(guiHelper),
                new SinteringFurnaceRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();
        registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.ALEMBIC).values(), AlembicRecipeCategory.ID);
        registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SPINNING_WHEEL).values(), SpinningWheelRecipeCategory.ID);
        registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SEASONING_BARREL).values(), SeasoningBarrelRecipeCategory.ID);
        registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.MOLTEN_CHAMBER).values(), SeasoningBarrelRecipeCategory.ID);
        registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SINTERING_FURNACE).values(), SinteringFurnaceRecipeCategory.ID);
        registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.MORTAR).values(), MortarRecipeCategory.ID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Workshop.MOD_ID, "default");
    }
}
