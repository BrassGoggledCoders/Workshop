package xyz.brassgoggledcoders.workshop.blocks.seasoningbarrel;


import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.fluid.SidedFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.item.DyeColor;
import xyz.brassgoggledcoders.workshop.blocks.WorkshopGUIMachine;
import xyz.brassgoggledcoders.workshop.recipes.PressRecipes;
import xyz.brassgoggledcoders.workshop.recipes.SeasoningBarrelRecipe;

import static xyz.brassgoggledcoders.workshop.blocks.BlockNames.SEASONING_BARREL_BLOCK;

public class SeasoningBarrelTile extends WorkshopGUIMachine {

    @Save
    private SidedInvHandler input;
    @Save
    private SidedFluidTank inputFluid;
    @Save
    private SidedInvHandler output;
    @Save
    private SidedFluidTank outputFluid;
    private SeasoningBarrelRecipe currentRecipe;


    public SeasoningBarrelTile() {
        super(SEASONING_BARREL_BLOCK, 102, 42, 100,  PosProgressBar.BarDirection.HORIZONTAL_RIGHT);
        this.addInventory(input = (SidedInvHandler) new SidedInvHandler("input", 34, 19, 1, 0).
                setColor(DyeColor.LIGHT_BLUE).
                setTile(this).
                setOnSlotChanged((stack, integer) -> checkForRecipe()));
        this.addTank(this.inputFluid = (SidedFluidTank) new SidedFluidTank("input_fluid", 4000, 33, 18, 1).
                setColor(DyeColor.LIME).
                setTankType(PosFluidTank.Type.SMALL).
                setTile(this).
                setTankAction(PosFluidTank.Action.FILL).
                setOnContentChange(this::checkForRecipe)
        );
        this.addInventory(this.output = (SidedInvHandler) new SidedInvHandler("output", 129, 22, 1, 2).
                setColor(DyeColor.ORANGE).
                setRange(1, 3).
                setInputFilter((stack, integer) -> false).
                setTile(this));
        this.addTank(this.outputFluid = (SidedFluidTank) new SidedFluidTank("output_fluid", 4000, 149, 20, 3).
                setColor(DyeColor.MAGENTA).
                setTile(this).
                setTankAction(PosFluidTank.Action.DRAIN));
    }

    @Override
    public Runnable onFinish() {
        return null;
    }

    private void checkForRecipe() {
        if (isServer()) {
            if (currentRecipe == null || !currentRecipe.matches(input, inputFluid)) {
                currentRecipe = RecipeUtil.getRecipes(world, SeasoningBarrelRecipe.SERIALIZER.getRecipeType()).stream().filter(barrelRecipe -> barrelRecipe.matches(input, inputFluid)).findFirst().orElse(null);
            }
        }
    }

}
