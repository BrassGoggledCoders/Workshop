package xyz.brassgoggledcoders.workshop.jei;

import com.hrznstudio.titanium.Titanium;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.datagen.language.WorkshopUSLanguageProvider;

import javax.annotation.Nonnull;

@JeiPlugin
@SuppressWarnings("unused")
public class WorkshopJEIPlugin implements IModPlugin {
    //TODO Category for displaying potential scrap bag loot
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                /*new AlembicRecipeCategory(guiHelper),
                new SpinningWheelRecipeCategory(guiHelper),
                new SinteringFurnaceRecipeCategory(guiHelper),
                new PressRecipeCategory(guiHelper),
                new MoltenChamberRecipeCategory(guiHelper),
                 */
                new SeasoningBarrelRecipeCategory(guiHelper),
                new DryingBasinRecipeCategory(guiHelper),
                new MortarRecipeCategory(guiHelper)
                );
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        if (Minecraft.getInstance().world != null) {
            final RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();
            /*registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.PRESS_SERIALIZER.get().getRecipeType()).values(), PressRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.ALEMBIC_SERIALIZER.get().getRecipeType()).values(), AlembicRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SPINNING_WHEEL_SERIALIZER.get().getRecipeType()).values(), SpinningWheelRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.MOLTEN_CHAMBER_SERIALIZER.get().getRecipeType()).values(), MoltenChamberRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SINTERING_FURNACE_SERIALIZER.get().getRecipeType()).values(), SinteringFurnaceRecipeCategory.ID);*/
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.MORTAR_SERIALIZER.get().getRecipeType()).values(), MortarRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SEASONING_BARREL_SERIALIZER.get().getRecipeType()).values(), SeasoningBarrelRecipeCategory.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.DRYING_BASIN_SERIALIZER.get().getRecipeType()).values(), DryingBasinRecipeCategory.ID);
        }
        registration.addIngredientInfo(new ItemStack(WorkshopItems.SCRAP_BAG.get()), VanillaTypes.ITEM, WorkshopUSLanguageProvider.SCRAP_BAG_DESC);
    }

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Workshop.MOD_ID, "default");
    }

    public static IDrawableAnimated getDefaultArrow(IGuiHelper guiHelper) {
        return guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
    }
}
