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
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlembicRecipeCategory implements IRecipeCategory<AlembicRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(WorkshopRecipes.ALEMBIC_SERIALIZER.get().getRecipeType().toString());

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public AlembicRecipeCategory(IGuiHelper guiHelper) {
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
    public Class<? extends AlembicRecipe> getRecipeClass() {
        return AlembicRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Alembic";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 100);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.ALEMBIC.getBlock()));
    }

    @Override
    public void draw(AlembicRecipe recipe, double mouseX, double mouseY) {
        //Inputs
        for (int i = 0; i < 3; i++) {
            slot.draw(34, 25 + (i * 17));
        }
        //Container
        slot.draw(56, 43);
        //Output
        slot.draw(102, 44);
        //Residue
        for(int i = 0; i < 3; i++) {
            slot.draw(125, 25 + (i * 17));
        }
        arrow.draw(24, 18);
    }

    @Override
    public void setIngredients(AlembicRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Arrays.asList(recipe.input));
        ingredients.setOutput(VanillaTypes.FLUID, recipe.output);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlembicRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        for(int i = 0; i < recipe.input.length; i++) {
            guiItemStacks.init(i, true, 29, 42 + (i * 17));
        }
        guiFluidStacks.init(4, false, 60, 10);
        for(int i = 5; i < recipe.residue.length; i++) {
            guiItemStacks.init(i, false, 29 + (i * 17), 70);
            //guiItemStacks.set(i, recipe.residue[i].stack);
        }

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }

    @Override
    public List<String> getTooltipStrings(AlembicRecipe recipe, double mouseX, double mouseY) {
        ArrayList<String> list = new ArrayList<>();
        if(mouseY < 70 && mouseX < 29) {
            //TODO Probably there's some fancy maths way to make this a oneliner :p
            //First slot
            int slot = 0;
            //Second Slot
            if(mouseX > (29 * 2)) {
               slot = 2;
            }
            //Third slot
            else if(mouseX > (29 * 3)) {
                slot = 3;
            }
            //Fourth Slot
            else if(mouseX > (29 * 4)) {
                slot = 4;
            }
            list.add("Min: " + recipe.residue[slot].min);
            list.add("Max: " + recipe.residue[slot].max);
        }
        return list;
    }
}
