package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.crafting.IRecipe;
import xyz.brassgoggledcoders.workshop.Workshop;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.SeasoningBarrelRecipe;

public class SeasoningBarrelTileEntity extends AbstractBarrelTileEntity<SeasoningBarrelTileEntity, SeasoningBarrelRecipe> {

    public SeasoningBarrelTileEntity() {
        super(WorkshopBlocks.SEASONING_BARREL.getTileEntityType(), new ProgressBarComponent<SeasoningBarrelTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
        this.inputFluidTank.setValidator(fluidStack -> fluidStack.getFluid().getFluid().getAttributes().getTemperature() < Fluids.LAVA.getAttributes().getTemperature());
        this.outputFluidTank.setValidator(fluidStack -> fluidStack.getFluid().getFluid().getAttributes().getTemperature() < Fluids.LAVA.getAttributes().getTemperature());
    }

    @Override
    public SeasoningBarrelTileEntity getSelf() {
        return this;
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.SEASONING_BARREL && recipe instanceof SeasoningBarrelRecipe;
    }

    @Override
    public SeasoningBarrelRecipe castRecipe(IRecipe<?> iRecipe) {
        return (SeasoningBarrelRecipe) iRecipe;
    }
}
