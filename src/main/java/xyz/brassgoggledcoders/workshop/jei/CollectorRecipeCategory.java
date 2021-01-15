package xyz.brassgoggledcoders.workshop.jei;

import com.hrznstudio.titanium.Titanium;
import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.util.text.StringTextComponent;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.recipe.CollectorRecipe;
import xyz.brassgoggledcoders.workshop.tileentity.CollectorTileEntity;
import xyz.brassgoggledcoders.workshop.util.RangedItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectorRecipeCategory implements IRecipeCategory<CollectorRecipe> {

    private final IGuiHelper guiHelper;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;

    public CollectorRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.drawableBuilder(new ResourceLocation(Titanium.MODID, "textures/gui/background.png"), 176, 60, 24, 17)
                .buildAnimated(500, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return CollectorTileEntity.ID;
    }

    @Override
    public Class<CollectorRecipe> getRecipeClass() {
        return CollectorRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Collector";
    }

    @Override
    public IDrawable getBackground() {
        return this.guiHelper.createBlankDrawable(160, 100);
    }

    @Override
    public IDrawable getIcon() {
        return this.guiHelper.createDrawableIngredient(new ItemStack(WorkshopBlocks.COLLECTOR.getBlock()));
    }

    @Override
    public void draw(CollectorRecipe recipe, MatrixStack stack, double mouseX, double mouseY) {
        //Inputs
        for (int i = 0; i < 3; i++) {
            slot.draw(stack, 5, 5 + (i * 18));
        }
        //Outputs
        for (int i = 0; i < CollectorTileEntity.outputSize; i++) {
            slot.draw(stack, 75, 5 + (i * 18));
        }
        arrow.draw(stack, 24, 24);
    }

    @Override
    public void setIngredients(CollectorRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Arrays.asList(recipe.input));
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < recipe.outputs.length; i++) {
            stacks.add(recipe.outputs[i].stack);
        }
        ingredients.setOutputs(VanillaTypes.ITEM, stacks);
    }

    //TODO Display of what tile entity types are required
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CollectorRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        int index = 0;
        guiItemStacks.init(index++, true, 5, 18);
        guiFluidStacks.init(0, false, 54, 38, 12, 16, 100, false, null);
        for (int i = 0; i < recipe.outputs.length; i++) {
            guiItemStacks.init(index + i, false, 75, 5 + (i * 18));
        }

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);

        int finalInputIndex = index;
        recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            int residueIndex = (slotIndex - finalInputIndex);
            if (slotIndex >= finalInputIndex && recipe.outputs.length >= residueIndex) {
                RangedItemStack rangedItemStack = recipe.outputs[residueIndex];
                if (rangedItemStack != null) {
                    if (rangedItemStack.min == rangedItemStack.max) {
                        tooltip.add(new StringTextComponent(String.format("Count: %s", rangedItemStack.min)));
                    } else {
                        tooltip.add(new StringTextComponent(String.format("Min: %s, Max: %s", rangedItemStack.min, rangedItemStack.max)));
                    }
                }
            }
        });
    }
}
