package xyz.brassgoggledcoders.workshop.tileentity;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.item.crafting.IRecipe;
import xyz.brassgoggledcoders.workshop.content.WorkshopBlocks;
import xyz.brassgoggledcoders.workshop.content.WorkshopRecipes;
import xyz.brassgoggledcoders.workshop.recipe.MoltenChamberRecipe;

public class MoltenChamberTileEntity extends AbstractBarrelTileEntity<MoltenChamberTileEntity, MoltenChamberRecipe> {
    public MoltenChamberTileEntity() {
        super(WorkshopBlocks.MOLTEN_CHAMBER.getTileEntityType(), new ProgressBarComponent<MoltenChamberTileEntity>(76, 42, 100).setBarDirection(ProgressBarComponent.BarDirection.HORIZONTAL_RIGHT));
    }

    @Override
    public MoltenChamberTileEntity getSelf() {
        return this;
    }

    @Override
    public boolean checkRecipe(IRecipe<?> recipe) {
        return recipe.getType() == WorkshopRecipes.MOLTEN_CHAMBER && recipe instanceof MoltenChamberRecipe;
    }

    @Override
    public MoltenChamberRecipe castRecipe(IRecipe<?> iRecipe) {
        return (MoltenChamberRecipe) iRecipe;
    }
}
