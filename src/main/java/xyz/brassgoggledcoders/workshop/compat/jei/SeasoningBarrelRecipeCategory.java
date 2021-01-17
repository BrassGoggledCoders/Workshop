package xyz.brassgoggledcoders.workshop.compat.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.SeasoningBarrelTileEntity;

public class SeasoningBarrelRecipeCategory extends AbstractBarrelRecipeCategory<SeasoningBarrelRecipe> {

    public SeasoningBarrelRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper);
    }

    @Override
    public ResourceLocation getUid() {
        return SeasoningBarrelTileEntity.ID;
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
