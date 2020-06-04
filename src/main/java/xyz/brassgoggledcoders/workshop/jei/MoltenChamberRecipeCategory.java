package xyz.brassgoggledcoders.workshop.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.MoltenChamberRecipe;

public class MoltenChamberRecipeCategory extends AbstractBarrelRecipeCategory {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.MOLTEN_CHAMBER_SERIALIZER.get().getRecipeType().toString());

    public MoltenChamberRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends MoltenChamberRecipe> getRecipeClass() {
        return MoltenChamberRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Molten Reaction Chamber";
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.MOLTEN_CHAMBER.getBlock()));
    }


}
