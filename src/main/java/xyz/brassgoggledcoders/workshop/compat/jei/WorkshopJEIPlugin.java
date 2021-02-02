package xyz.brassgoggledcoders.workshop.compat.jei;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.client.screen.addon.ProgressBarScreenAddon;
import com.hrznstudio.titanium.client.screen.container.BasicAddonScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopItems;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.tileentity.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

@JeiPlugin
@SuppressWarnings("unused")
public class WorkshopJEIPlugin implements IModPlugin {

    //TODO Category for displaying potential scrap bag loot
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                //new AlembicRecipeCategory(guiHelper),
                new SpinningWheelRecipeCategory(guiHelper),
                new SinteringFurnaceRecipeCategory(guiHelper),
                new PressRecipeCategory(guiHelper),
                new MoltenChamberRecipeCategory(guiHelper),
                new SeasoningBarrelRecipeCategory(guiHelper),
                new DryingBasinRecipeCategory(guiHelper),
                new MortarRecipeCategory(guiHelper),
                new CollectorRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        if (Minecraft.getInstance().world != null) {
            final RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.PRESS_SERIALIZER.get().getRecipeType()).values(), PressTileEntity.ID);
            //registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.ALEMBIC_SERIALIZER.get().getRecipeType()).values(), AlembicTileEntity.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SPINNING_WHEEL_SERIALIZER.get().getRecipeType()).values(), SpinningWheelTileEntity.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.MOLTEN_CHAMBER_SERIALIZER.get().getRecipeType()).values(), MoltenChamberTileEntity.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SINTERING_FURNACE_SERIALIZER.get().getRecipeType()).values(), SinteringFurnaceTileEntity.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.MORTAR_SERIALIZER.get().getRecipeType()).values(), MortarTileEntity.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.SEASONING_BARREL_SERIALIZER.get().getRecipeType()).values(), SeasoningBarrelTileEntity.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.DRYING_BASIN_SERIALIZER.get().getRecipeType()).values(), DryingBasinTileEntity.ID);
            registration.addRecipes(recipeManager.getRecipes(WorkshopRecipes.COLLECTOR_SERIALIZER.get().getRecipeType()).values(), CollectorTileEntity.ID);
        }
        registration.addIngredientInfo(new ItemStack(WorkshopItems.SCRAP_BAG.get()), VanillaTypes.ITEM, Workshop.SCRAP_BAG_DESC);
        registration.addIngredientInfo(new ItemStack(WorkshopBlocks.FLUID_FUNNEL.get()), VanillaTypes.ITEM, Workshop.FLUID_FUNNEL_DESC);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(BasicAddonScreen.class, new IGuiContainerHandler<BasicAddonScreen>() {
            @Override
            @Nonnull
            public Collection<IGuiClickableArea> getGuiClickableAreas(@Nonnull BasicAddonScreen containerScreen, double mouseX, double mouseY) {
                Collection<IGuiClickableArea> areas = new ArrayList<>();
                if (containerScreen.getContainer().getProvider() instanceof BasicMachineTileEntity) {
                    containerScreen.getAddons().stream()
                            .filter(addon -> addon instanceof ProgressBarScreenAddon)
                            .map(addon -> (ProgressBarScreenAddon<?>) addon)
                            .forEach(addon -> areas.add(IGuiClickableArea.createBasic(addon.getPosX(), addon.getPosY(), addon.getXSize(), addon.getYSize(), ((BasicMachineTileEntity<?, ?>) containerScreen.getContainer().getProvider()).getRecipeCategoryUID())));
                }
                return areas;
            }
        });
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
