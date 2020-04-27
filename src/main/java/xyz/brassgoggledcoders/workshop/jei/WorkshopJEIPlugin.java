package xyz.brassgoggledcoders.workshop.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;

@JeiPlugin
public class WorkshopJEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new SpinningWheelRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new SeasoningBarrelRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
                new SinteringFurnaceRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes(WorkshopRecipes.SPINNING_WHEEL).values(), SpinningWheelRecipeCategory.ID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes(WorkshopRecipes.SEASONING_BARREL).values(), SeasoningBarrelRecipeCategory.ID);
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes(WorkshopRecipes.SINTERING_FURNACE).values(), SinteringFurnaceRecipeCategory.ID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Workshop.MOD_ID, "default");
    }
}
