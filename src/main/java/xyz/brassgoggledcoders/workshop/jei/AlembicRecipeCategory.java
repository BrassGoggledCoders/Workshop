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
import xyz.brassgoggledcoders.workshop.recipe.AlembicRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.AlembicTileEntity;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlembicRecipeCategory implements IRecipeCategory<AlembicRecipe> {

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;
    private final IDrawable tank;

    public AlembicRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
        this.tank = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 0, 20, 58).build();
    }

    @Override
    public ResourceLocation getUid() {
        return AlembicTileEntity.ID;
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
            slot.draw(5, 5 + (i * 18));
        }
        //Outputs
        this.tank.draw(50, 0);
        for (int i = 0; i < AlembicTileEntity.residueSize; i++) {
            slot.draw(75, 5 + (i * 18));
        }
        arrow.draw(24, 24);
    }

    @Override
    public void setIngredients(AlembicRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Arrays.asList(recipe.input));
        if (!recipe.output.isEmpty()) {
            ingredients.setOutput(VanillaTypes.FLUID, recipe.output);
        }
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < recipe.residue.length; i++) {
            stacks.add(recipe.residue[i].stack);
        }
        ingredients.setOutputs(VanillaTypes.ITEM, stacks);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlembicRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        int index = 0;
        for (int i = 0; i < recipe.input.length; i++) {
            guiItemStacks.init(index++, true, 5, 5 + (i * 18));
        }
        guiFluidStacks.init(0, false, 54, 38, 12, 16, 100, false, null);
        for (int i = 0; i < recipe.residue.length; i++) {
            guiItemStacks.init(index + i, false, 75, 5 + (i * 18));
        }

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);

        int finalInputIndex = index;
        recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            int residueIndex = (slotIndex - finalInputIndex);
            if (slotIndex >= finalInputIndex && recipe.residue.length >= residueIndex) {
                RangedItemStack rangedItemStack = recipe.residue[residueIndex];
                if (rangedItemStack != null) {
                    if (rangedItemStack.min == rangedItemStack.max) {
                        tooltip.add(String.format("Count: %s", rangedItemStack.min));
                    } else {
                        tooltip.add(String.format("Min: %s, Max: %s", rangedItemStack.min, rangedItemStack.max));
                    }
                }
            }
        });
    }
}
