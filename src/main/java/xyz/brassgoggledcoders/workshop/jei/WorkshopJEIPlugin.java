package xyz.brassgoggledcoders.workshop.jei;

import com.hrznstudio.titanium.Titanium;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.MoltenChamberRecipe;

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
                new SinteringFurnaceRecipeCategory(guiHelper),
                new PressRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if(Minecraft.getInstance().world != null) {
            final RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.ALEMBIC_SERIALIZER.get().getRecipeType()).values(), AlembicRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SPINNING_WHEEL_SERIALIZER.get().getRecipeType()).values(), SpinningWheelRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SEASONING_BARREL_SERIALIZER.get().getRecipeType()).values(), SeasoningBarrelRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.MOLTEN_CHAMBER_SERIALIZER.get().getRecipeType()).values(), MoltenChamberRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SINTERING_FURNACE_SERIALIZER.get().getRecipeType()).values(), SinteringFurnaceRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.MORTAR_SERIALIZER.get().getRecipeType()).values(), MortarRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.PRESS_SERIALIZER.get().getRecipeType()).values(), PressRecipeCategory.ID);
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Workshop.MOD_ID, "default");
    }

    public static IDrawableAnimated getDefaultArrow(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
    }
}
