package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;


import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.fluid.SidedFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import net.minecraft.item.DyeColor;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;
import xyz.brassgoggledcoders.workshop.recipes.SeasoningBarrelRecipe;
import xyz.brassgoggledcoders.workshop.registries.Recipes;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SEASONING_BARREL_BLOCK;

public class SeasoningBarrelTile extends WorkshopGUIMachine {

    @Save
    private SidedInvHandler input;
    @Save
    private SidedFluidTank fluidSlot;
    @Save
    private SidedInvHandler output;

    private SeasoningBarrelRecipe currentRecipe;


    public SeasoningBarrelTile() {
        super(SEASONING_BARREL_BLOCK, 58, 40, 100, PosProgressBar.BarDirection.VERTICAL_UP);
        this.addInventory(input = (SidedInvHandler) new SidedInvHandler("input", 58, 10, 1, 0).
                setColor(DyeColor.LIGHT_BLUE).
                setTile(this).
                setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addTank(this.fluidSlot = (SidedFluidTank) new SidedFluidTank("input_fluid", 4000, 103, 20, 1)
                .setColor(DyeColor.LIME)
                .setTile(this)
                .setTankAction(PosFluidTank.Action.FILL)
                .setOnContentChange(this::checkForRecipe));
        this.addInventory(this.output = (SidedInvHandler) new SidedInvHandler("output", 58, 60, 1, 2)
                .setColor(DyeColor.ORANGE)
                .setInputFilter((stack, integer) -> false)
                .setTile(this));
    }

    @Override
    public Runnable onFinish() {
        return null;
    }

    private void checkForRecipe() {
        if (isServer()) {
            if (currentRecipe == null || !currentRecipe.matches(input,fluidSlot)) {
                currentRecipe = this.getWorld().getRecipeManager()
                        .getRecipes()
                        .stream()
                        .filter(recipe -> recipe.getType() == Recipes.PRESS)
                        .map(recipe -> (SeasoningBarrelRecipe) recipe)
                        .filter(this::matches)
                        .findFirst()
                        .orElse(null);
            }
        }
    }

    private boolean matches(SeasoningBarrelRecipe seasoningBarrelRecipe) {
        return seasoningBarrelRecipe.matches(input, fluidSlot);
    }
}
